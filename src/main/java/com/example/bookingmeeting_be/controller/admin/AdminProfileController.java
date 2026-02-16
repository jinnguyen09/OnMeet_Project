package com.example.bookingmeeting_be.controller.admin;

import com.example.bookingmeeting_be.model.Users;
import com.example.bookingmeeting_be.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/admin/profile")
public class AdminProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showProfile(Principal principal, Model model) {
        Users user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        return "admin/admin-profile";
    }

    @PostMapping("/update")
    public String updateProfile(Principal principal,
                                @RequestParam String fullname,
                                @RequestParam String phone,
                                @RequestParam(required = false) String address,
                                @RequestParam(required = false) String company,
                                @RequestParam(required = false) String jobTitle,
                                @RequestParam(required = false) String about,
                                @RequestParam("multipartFile") MultipartFile file,
                                RedirectAttributes ra) throws IOException {

        Users user = userService.findByEmail(principal.getName());

        userService.updateProfile(user, fullname, phone, address, company, jobTitle, about, file);

        ra.addFlashAttribute("success", "Cập nhật hồ sơ thành công!");
        return "redirect:/admin/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(Principal principal,
                                 @RequestParam String currentPass,
                                 @RequestParam String newPass,
                                 RedirectAttributes ra) {

        Users user = userService.findByEmail(principal.getName());
        boolean success = userService.changePassword(user, currentPass, newPass);

        if (success) {
            ra.addFlashAttribute("success", "Đổi mật khẩu thành công!");
        } else {
            ra.addFlashAttribute("error", "Mật khẩu hiện tại không chính xác!");
        }
        return "redirect:/admin/profile";
    }
}