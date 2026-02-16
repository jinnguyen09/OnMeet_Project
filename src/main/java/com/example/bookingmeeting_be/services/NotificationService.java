package com.example.bookingmeeting_be.services;

import com.example.bookingmeeting_be.model.Notification;
import com.example.bookingmeeting_be.model.NotificationRecipient;
import com.example.bookingmeeting_be.model.NotificationRecipientId;
import com.example.bookingmeeting_be.repository.BookingAttendeeRepository;
import com.example.bookingmeeting_be.repository.NotificationRecipientRepository;
import com.example.bookingmeeting_be.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationRecipientRepository notificationRecipientRepository;
    @Autowired
    private BookingAttendeeRepository bookingAttendeeRepository;

    public long getUnreadCount(Integer userId) {
        return notificationRecipientRepository.countByIdUserIdAndIsReadFalse(userId);
    }

    public List<NotificationRecipient> getInbox(Integer userId) {
        return notificationRecipientRepository.findInbox(userId);
    }

    //    @Transactional
//    public void markRead(Integer userId, Integer notiId) {
//        NotificationRecipientId id = new NotificationRecipientId(notiId, userId);
//        notificationRecipientRepository.findById(id).ifPresent(nr -> {
//            nr.setIsRead(true);
//            notificationRecipientRepository.save(nr);
//        });
//    }
    @Transactional
    public void markRead(Integer userId, Integer notiId) {
        notificationRecipientRepository.markRead(userId, notiId);
    }

    @Transactional
    public void acceptInvite(Integer userId, Integer notiId) {
        Notification noti = notificationRepository.findById(notiId)
                .orElseThrow();

        int updated = bookingAttendeeRepository.updateStatus(
                noti.getBookingId(),
                userId,
                "ACCEPTED"
        );
        if (updated == 0) {
            throw new RuntimeException("Bạn không có lời mời hoặc trạng thái mời không hợp lệ");
        }


        markRead(userId, notiId);
    }

    @Transactional
    public void declineInvite(Integer userId, Integer notiId) {
        Notification noti = notificationRepository.findById(notiId)
                .orElseThrow();

        int updated = bookingAttendeeRepository.updateStatus(
                noti.getBookingId(), userId, "DECLINED"
        );
        if (updated == 0) {
            throw new RuntimeException("Bạn đã phản hồi hoặc không được mời");
        }


        markRead(userId, notiId);
    }
    public java.util.Map<String, String> getAttendeeStatusMap(Integer userId, List<NotificationRecipient> inbox) {
        List<Integer> bookingIds = inbox.stream()
                .map(nr -> nr.getNotification().getBookingId())
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();

        if (bookingIds.isEmpty()) return java.util.Collections.emptyMap();

        return bookingAttendeeRepository.findStatusesByUserAndBookingIds(userId, bookingIds)
                .stream()
                .collect(java.util.stream.Collectors.toMap(
                        r -> String.valueOf(r[0]),
                        r -> {
                            String s = (String) r[1];
                            return s == null ? null : s.trim().toUpperCase();
                        },
                        (oldVal, newVal) -> newVal
                ));
    }



}
