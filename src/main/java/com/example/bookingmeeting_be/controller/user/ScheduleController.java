package com.example.bookingmeeting_be.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ScheduleController {
    @GetMapping("/schedule")
    public String schedule(Model model) {
        model.addAttribute("activePage", "schedule");
        return "users/schedule";
    }
}
