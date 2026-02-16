package com.example.bookingmeeting_be.controller.user;

import com.example.bookingmeeting_be.model.Users;
import com.example.bookingmeeting_be.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String showProfile(Model model, Authentication authentication) {
        String email = authentication.getName();
        Users user = userService.findByEmail(email);

        model.addAttribute("user", user);
        model.addAttribute("activePage", "profile");
        return "users/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam("fullName") String fullName,
                                @RequestParam("phone") String phone,
                                @RequestParam("address") String address,
                                @RequestParam("company") String company,
                                @RequestParam(value = "jobTitle", required = false) String jobTitle,
                                @RequestParam("about") String about,
                                @RequestParam("profileImage") MultipartFile multipartFile,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        try {
            Users user = userService.findByEmail(authentication.getName());
            userService.updateProfile(user, fullName, phone, address, company, jobTitle, about, multipartFile);
            redirectAttributes.addFlashAttribute("message", "Cập nhật hồ sơ thành công!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi upload ảnh.");
        }
        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("password") String currentPassword,
                                 @RequestParam("newpassword") String newPassword,
                                 @RequestParam("renewpassword") String renewPassword,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(renewPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không khớp!");
            return "redirect:/profile";
        }

        Users user = userService.findByEmail(authentication.getName());
        boolean success = userService.changePassword(user, currentPassword, newPassword);

        if (success) {
            redirectAttributes.addFlashAttribute("message", "Đổi mật khẩu thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không đúng!");
        }

        return "redirect:/profile";
    }
}