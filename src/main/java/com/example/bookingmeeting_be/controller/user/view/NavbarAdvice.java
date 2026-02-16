package com.example.bookingmeeting_be.controller.user.view;

import com.example.bookingmeeting_be.services.NotificationService;
import com.example.bookingmeeting_be.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class NavbarAdvice {

    private final NotificationService notificationService;
    private final UserService userService;

    private Integer currentUserId(Authentication auth) {
        return userService.findByEmail(auth.getName()).getUserId();
    }

    @ModelAttribute
    public void addNavbarNotis(Model model, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return;

        Integer userId = currentUserId(auth);
        var inbox = notificationService.getInbox(userId); // hoặc tạo method getInboxLimit(userId, 5)
        var navNotis = inbox.stream().limit(5).toList();

        model.addAttribute("navUnreadCount", notificationService.getUnreadCount(userId));
        model.addAttribute("navNotis", navNotis);
        model.addAttribute("navStatusMap", notificationService.getAttendeeStatusMap(userId, navNotis));
        // nhớ: getAttendeeStatusMap nên trả Map<String,String> như mình đã hướng dẫn
    }
    @ModelAttribute("navFullname")
    public String navFullname(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return null;

        String email = authentication.getName(); // đang là email/username
        try {
            return userService.findByEmail(email).getFullname();
        } catch (Exception e) {
            return email; // fallback nếu không có user
        }
    }
}
