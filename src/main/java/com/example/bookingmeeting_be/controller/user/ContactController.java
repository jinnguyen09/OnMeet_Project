package com.example.bookingmeeting_be.controller.user;

import com.example.bookingmeeting_be.services.UserService;
import com.example.bookingmeeting_be.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ContactController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/contact")
    public String showContactPage(Model model, Principal principal) {
        if (principal != null) {

            Users user = userService.findByEmail(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("activePage", "contact");
        }
        return "users/contact";
    }
}