package com.example.bookingmeeting_be.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EventsController {
    @GetMapping("users/events")
    public String sukien(Model model) {
        model.addAttribute("activePage", "events");
        return "users/events";
    }
}
