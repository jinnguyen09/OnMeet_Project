package com.example.bookingmeeting_be.controller.user.view;

import com.example.bookingmeeting_be.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("userDashboardController")
@RequiredArgsConstructor
@RequestMapping("/users")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboardService.getDashboardStats());
        model.addAttribute("activePage", "dashboard");

        return "users/dashboard";
    }
}

