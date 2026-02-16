package com.example.bookingmeeting_be.controller.admin.view;

import com.example.bookingmeeting_be.model.dto.DashboardStats;
import com.example.bookingmeeting_be.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequiredArgsConstructor
@Controller("adminDashboardController")
@RequestMapping("/admin")
public class AdminHomeController {

    private final DashboardService dashboardService;

    @GetMapping({"/home", ""})
    public String dashboard(Model model) {
        DashboardStats stats = dashboardService.getDashboardStats();

        model.addAttribute("stats", stats);

        model.addAttribute("activePage", "dashboard");

        return "admin/admin-home";
    }
}
