package com.example.bookingmeeting_be.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users")
@Controller
public class HomeController {
    @GetMapping({"/home", "/"})
    public String home(Model model) {
        model.addAttribute("activePage", "home");
        return "users/home";
    }

    @GetMapping("/terms")
    public String terms(Model model) {
        model.addAttribute("activePage", "terms");
        return "users/terms";
    }

    @GetMapping("/policy")
    public String policy(Model model) {
        model.addAttribute("activePage", "policy");
        return "users/policy";
    }
}
