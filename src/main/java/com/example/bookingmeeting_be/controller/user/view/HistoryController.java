package com.example.bookingmeeting_be.controller.user.view;

import com.example.bookingmeeting_be.model.Booking;
import com.example.bookingmeeting_be.model.Users;
import com.example.bookingmeeting_be.services.BookingService;
import com.example.bookingmeeting_be.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class HistoryController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @GetMapping("/history")
    public String history(Model model) {
        model.addAttribute("activePage", "history");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Users currentUser = userService.findByEmail(email);

        if (currentUser != null) {
            List<Booking> hostedBookings = bookingService.getMyHostedBookings(currentUser.getUserId());
            List<Booking> invitedBookings = bookingService.getMyInvitedBookings(currentUser.getUserId());
            model.addAttribute("hostedBookings", hostedBookings);
            model.addAttribute("invitedBookings", invitedBookings);
        }

        return "users/history";
    }

    @PostMapping("/history/cancel/{id}")
    public String cancelBooking(@PathVariable("id") Integer bookingId, RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(bookingId);
            redirectAttributes.addFlashAttribute("successMessage", "Cuộc họp đã được huỷ!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xoá cuộc họp: " + e.getMessage());
        }

        return "redirect:/users/history";
    }

    @GetMapping("/history/detail/{id}")
    public String getBookingDetail(@PathVariable Integer id, Model model) {
        Booking booking = bookingService.getBookingById(id);

        model.addAttribute("b", booking);
        return "users/booking-detail-view";
    }
}
