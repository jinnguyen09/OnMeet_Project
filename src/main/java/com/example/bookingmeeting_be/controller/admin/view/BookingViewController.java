package com.example.bookingmeeting_be.controller.admin.view;

import com.example.bookingmeeting_be.model.Booking;
import com.example.bookingmeeting_be.model.Device;
import com.example.bookingmeeting_be.model.dto.BookingRequest;
import com.example.bookingmeeting_be.model.dto.UserResponse;
import com.example.bookingmeeting_be.repository.BookingAttendeeRepository;
import com.example.bookingmeeting_be.services.BookingService;
import com.example.bookingmeeting_be.services.DeviceService;
import com.example.bookingmeeting_be.services.MeetingRoomService;
import com.example.bookingmeeting_be.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/bookings")
public class BookingViewController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingAttendeeRepository bookingAttendeeRepository;

    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserService userService;

    @GetMapping("/api/users/search")
    @ResponseBody
    public List<UserResponse> searchUsersApi(@RequestParam("q") String q) {
        return userService.listUsers(q, 0, 10).getContent();
    }

    @GetMapping
    public String listBookings(@RequestParam(value = "userId", required = false) Integer userId, Model model) {
        List<Booking> bookings = (userId != null)
                ? bookingService.getBookingsByUser(userId)
                : bookingService.getAllBookings();

        Map<Integer, String> roomNames = new HashMap<>();
        meetingRoomService.getAll().forEach(r -> roomNames.put(r.getId(), r.getName()));

        Map<Integer, String> hostNames = new HashMap<>();
        Map<Integer, String> hostEmails = new HashMap<>();
        Map<Integer, String> hostPhones = new HashMap<>();

        userService.findAll().forEach(u -> {
            hostNames.put(u.getUserId(), u.getFullname());
            hostEmails.put(u.getUserId(), u.getEmail());
            hostPhones.put(u.getUserId(), u.getPhone());
        });

        Map<Integer, Long> attendeeCounts = new HashMap<>();
        bookingAttendeeRepository.countAcceptedGroupByBooking().forEach(row -> {
            attendeeCounts.put(((Number) row[0]).intValue(), ((Number) row[1]).longValue());
        });

        Map<Integer, Long> totalCounts = new HashMap<>();
        bookingAttendeeRepository.countAllGroupByBooking().forEach(row -> {
            totalCounts.put(((Number) row[0]).intValue(), ((Number) row[1]).longValue());
        });

        model.addAttribute("bookings", bookings);
        model.addAttribute("attendeeCounts", attendeeCounts);
        model.addAttribute("invitedCounts", totalCounts);

        model.addAttribute("roomNames", roomNames);
        model.addAttribute("hostNames", hostNames);
        model.addAttribute("hostEmails", hostEmails);
        model.addAttribute("hostPhones", hostPhones);

        return "admin/booking-list";
    }

    @GetMapping("/create")
    public String showBookingForm(Model model) {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setDevices(new ArrayList<>());

        model.addAttribute("isEdit", false);
        model.addAttribute("bookingRequest", bookingRequest);
        model.addAttribute("rooms", meetingRoomService.getAllWithAssets());
        model.addAttribute("availableDevices", deviceService.getAvailableDevices());

        return "admin/booking-form";
    }

    @PostMapping("/save")
    public String saveBooking(@ModelAttribute("bookingRequest") BookingRequest bookingRequest,
                              RedirectAttributes redirectAttributes) {
        try {
            bookingService.createBooking(bookingRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Đặt phòng thành công!");
            return "redirect:/admin/bookings";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/admin/bookings/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String editBookingForm(@PathVariable("id") Integer id, Model model) {
        Booking booking = bookingService.getBookingById(id);

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setTitle(booking.getTitle());
        bookingRequest.setRoomId(booking.getRoomId());
        bookingRequest.setHostUserId(booking.getHostUserId());
        bookingRequest.setStartTime(booking.getStartTime());
        bookingRequest.setEndTime(booking.getEndTime());
        bookingRequest.setDescription(booking.getDescription());
        bookingRequest.setDevices(new ArrayList<>());


        model.addAttribute("isEdit", true);
        model.addAttribute("bookingId", id);
        model.addAttribute("bookingRequest", bookingRequest);
        model.addAttribute("rooms", meetingRoomService.getAllWithAssets());
        model.addAttribute("availableDevices", deviceService.getAvailableDevices());

        return "admin/booking-form";
    }

    @PostMapping("/{id}/edit")
    public String updateBooking(@PathVariable("id") Integer id,
                                @ModelAttribute("bookingRequest") BookingRequest bookingRequest,
                                RedirectAttributes redirectAttributes) {
        try {
            bookingService.updateBooking(id, bookingRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công!");
            return "redirect:/admin/bookings";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/bookings/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã hủy lịch họp và hoàn trả thiết bị!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hủy thất bại: " + e.getMessage());
        }
        return "redirect:/admin/bookings";
    }


    @PostMapping("/{id}/confirm")
    public String confirmBooking(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.updateStatus(id, "CONFIRMED");
            redirectAttributes.addFlashAttribute("successMessage", "Đã duyệt lịch họp!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Duyệt thất bại: " + e.getMessage());
        }
        return "redirect:/admin/bookings";
    }
}