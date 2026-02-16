package com.example.bookingmeeting_be.controller.user.view;

import com.example.bookingmeeting_be.model.NotificationRecipient;
import com.example.bookingmeeting_be.services.NotificationService;
import com.example.bookingmeeting_be.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users/notifications")
public class NotificationController {

    @Autowired private NotificationService notificationService;
    @Autowired private UserService userService;

    private Integer currentUserId(Authentication auth) {
        // Cauth.getName() là email
        return userService.findByEmail(auth.getName()).getUserId();

    }

    @GetMapping
    public String inbox(Authentication auth, Model model) {
        Integer userId = currentUserId(auth);

        List<NotificationRecipient> inbox = notificationService.getInbox(userId);
        long unread = notificationService.getUnreadCount(userId);

        model.addAttribute("unreadCount", unread);
        model.addAttribute("statusMap", notificationService.getAttendeeStatusMap(userId, inbox));

        var inboxByDay = inbox.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        nr -> nr.getNotification().getCreatedAt().toLocalDate(),
                        java.util.LinkedHashMap::new,
                        java.util.stream.Collectors.toList()
                ));
        model.addAttribute("debugUserId", userId);
        model.addAttribute("inboxByDay", inboxByDay);
        return "users/notifications";
    }


    @PostMapping("/{notiId}/read")
    public String markRead(@PathVariable Integer notiId, Authentication auth) {
        Integer userId = currentUserId(auth);
        notificationService.markRead(userId, notiId);
        return "redirect:/users/notifications";
    }

    @PostMapping("/{notiId}/accept")
    public String accept(@PathVariable Integer notiId, Authentication auth, RedirectAttributes ra) {
        Integer userId = currentUserId(auth);
        try {
            notificationService.acceptInvite(userId, notiId);
            ra.addFlashAttribute("msg", "Bạn đã đồng ý lời mời.");
        } catch (Exception e) {
            ra.addFlashAttribute("err", e.getMessage());
        }
        return "redirect:/users/notifications";
    }

    @PostMapping("/{notiId}/decline")
    public String decline(@PathVariable Integer notiId, Authentication auth, RedirectAttributes ra) {
        Integer userId = currentUserId(auth);
        try {
            notificationService.declineInvite(userId, notiId);
            ra.addFlashAttribute("msg", "Bạn đã từ chối lời mời.");
        } catch (Exception e) {
            ra.addFlashAttribute("err", e.getMessage());
        }
        return "redirect:/users/notifications";
    }
}
