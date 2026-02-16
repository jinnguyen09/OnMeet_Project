package com.example.bookingmeeting_be.services;

import com.example.bookingmeeting_be.model.dto.BookingRequest;
import com.example.bookingmeeting_be.model.*;
import com.example.bookingmeeting_be.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingDeviceRepository bookingDeviceRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private BookingAttendeeRepository bookingAttendeeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationRecipientRepository notificationRecipientRepository;
    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Transactional
    public Booking createBooking(BookingRequest request) {
        validateBookingTime(request.getStartTime(), request.getEndTime());

        boolean isConflict = bookingRepository.existsOverlappingBooking(
                request.getRoomId(), request.getStartTime(), request.getEndTime());
        if (isConflict) {
            throw new RuntimeException("Phòng họp đã có người đặt trong khoảng thời gian này!");
        }

        MeetingRoom room = meetingRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng họp!"));

        int guestCount = (request.getAttendeeUserIds() == null) ? 0 : request.getAttendeeUserIds().size();
        boolean hostCounts = (request.getIsHostParticipating() == null) || request.getIsHostParticipating();
        int totalAttendees = guestCount + (hostCounts ? 1 : 0);

        if (totalAttendees > room.getCapacity()) {
            throw new RuntimeException("Vượt quá sức chứa của phòng (" + room.getCapacity() + " người)!");
        }

        Booking booking = new Booking();
        booking.setTitle(request.getTitle());
        booking.setDescription(request.getDescription());
        booking.setRoomId(request.getRoomId());
        booking.setHostUserId(request.getHostUserId());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setStatus("PENDING");

        if (room.getPricePerHour() != null) {
            long minutes = java.time.Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();
            double hours = minutes / 60.0;
            booking.setTotalPrice(hours * room.getPricePerHour());
        }

        Booking savedBooking = bookingRepository.save(booking);

        processDevices(savedBooking, request.getDevices());

        syncAttendeesAndNotify(savedBooking, request.getAttendeeUserIds(), false);

        return savedBooking;
    }

    @Transactional
    public Booking updateBooking(Integer bookingId, BookingRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch họp!"));

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("Không thể sửa lịch họp đã bị hủy!");
        }

        validateBookingTime(request.getStartTime(), request.getEndTime());

        boolean conflict = bookingRepository.existsOverlappingBookingForUpdate(
                bookingId, request.getRoomId(), request.getStartTime(), request.getEndTime());
        if (conflict) {
            throw new RuntimeException("Thời gian mới bị trùng với lịch họp khác!");
        }

        returnDevicesToStock(bookingId);

        MeetingRoom room = meetingRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng họp!"));
        booking.setTitle(request.getTitle());
        booking.setRoomId(request.getRoomId());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setDescription(request.getDescription());
        Booking updatedBooking = bookingRepository.save(booking);

        if (room.getPricePerHour() != null) {
            long minutes = java.time.Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();
            double hours = minutes / 60.0;
            booking.setTotalPrice(hours * room.getPricePerHour());
        }

        processDevices(updatedBooking, request.getDevices());

        syncAttendeesAndNotify(updatedBooking, request.getAttendeeUserIds(), true);

        return updatedBooking;
    }

    @Transactional
    public void cancelBooking(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch họp!"));

        if ("CANCELLED".equals(booking.getStatus())) return;

        returnDevicesToStock(bookingId);

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }

    private void validateBookingTime(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) throw new RuntimeException("Thời gian không được để trống");
        if (!start.isBefore(end)) throw new RuntimeException("Giờ kết thúc phải sau giờ bắt đầu");
        if (start.isBefore(LocalDateTime.now())) throw new RuntimeException("Không thể đặt lịch trong quá khứ");
    }

    private void processDevices(Booking booking, List<BookingRequest.DeviceRequest> deviceRequests) {
        if (deviceRequests == null) return;

        for (BookingRequest.DeviceRequest item : deviceRequests) {
            if (item.getDeviceId() == null || item.getQuantity() <= 0) continue;

            Device device = deviceRepository.findById(item.getDeviceId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thiết bị ID: " + item.getDeviceId()));

            if (device.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Thiết bị " + device.getName() + " không đủ số lượng trong kho!");
            }

            device.setQuantity(device.getQuantity() - item.getQuantity());
            deviceRepository.save(device);

            BookingDevice bd = new BookingDevice(booking, device, item.getQuantity());
            bookingDeviceRepository.save(bd);
        }
    }

    private void returnDevicesToStock(Integer bookingId) {
        List<BookingDevice> currentDevices = bookingDeviceRepository.findByBookingId(bookingId);
        for (BookingDevice bd : currentDevices) {
            Device device = bd.getDevice();
            device.setQuantity(device.getQuantity() + bd.getQuantity());
            deviceRepository.save(device);
        }
        bookingDeviceRepository.deleteByBookingId(bookingId);
    }

    @Transactional
    public void syncAttendeesAndNotify(Booking booking, List<Integer> attendeeIds, boolean isUpdate) {
        if (isUpdate) {
            bookingAttendeeRepository.deleteByBookingAttendeeIdBookingId(booking.getId());
        }

        if (attendeeIds == null || attendeeIds.isEmpty()) return;

        List<Integer> normalized = attendeeIds.stream()
                .filter(id -> id != null && !id.equals(booking.getHostUserId()))
                .distinct()
                .collect(Collectors.toList());

        if (normalized.isEmpty()) return;

        for (Integer uid : normalized) {
            Users u = userRepository.findById(uid).orElseThrow();
            BookingAttendee ba = new BookingAttendee();
            ba.setBookingAttendeeId(new BookingAttendeeId(booking.getId(), uid));
            ba.setBooking(booking);
            ba.setUsers(u);
            ba.setStatus("INVITED");
            bookingAttendeeRepository.save(ba);
        }

        Notification noti = new Notification();
        noti.setBookingId(booking.getId());
        noti.setType(isUpdate ? "BOOKING_UPDATED" : "INVITE");
        noti.setContent((isUpdate ? "Cập nhật: " : "Lời mời họp: ") + booking.getTitle());
        noti.setCreatedAt(LocalDateTime.now());
        Notification savedNoti = notificationRepository.save(noti);

        List<NotificationRecipient> recipients = new ArrayList<>();
        for (Integer uid : normalized) {
            NotificationRecipient nr = new NotificationRecipient();
            nr.setNotification(savedNoti);
            nr.setUser(userRepository.getReferenceById(uid));
            nr.setId(new NotificationRecipientId(savedNoti.getNotificationId(), uid));
            nr.setIsRead(false);
            recipients.add(nr);
        }
        notificationRecipientRepository.saveAll(recipients);
    }

    @Transactional
    public void updateStatus(Integer id, String status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch họp với ID: " + id));
        booking.setStatus(status);
        bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() { return bookingRepository.findAll(); }
    public Booking getBookingById(Integer id) { return bookingRepository.findById(id).orElseThrow(); }
    public List<Booking> getBookingsByUser(Integer userId) { return bookingRepository.findByHostUserIdOrderByStartTimeDesc(userId); }

    public List<Booking> getMyHostedBookings(Integer userId) {
        return bookingRepository.findByHostUserIdOrderByStartTimeDesc(userId);
    }

    public List<Booking> getMyInvitedBookings(Integer userId) {
        return bookingRepository.findBookingsByAttendee(userId);
    }
}