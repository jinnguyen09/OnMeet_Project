package com.example.bookingmeeting_be.config;

import com.example.bookingmeeting_be.model.Booking;
import com.example.bookingmeeting_be.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;

@ControllerAdvice(basePackages = "com.example.bookingmeeting_be.controller.admin")
public class AdminGlobalAdvice {

    @Autowired
    private BookingRepository bookingRepo;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        List<Booking> pending = bookingRepo.findByStatus("PENDING");
        model.addAttribute("pendingCount", pending.size());
        model.addAttribute("pendingNotifications", pending);
    }
}