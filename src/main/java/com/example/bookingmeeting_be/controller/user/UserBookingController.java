package com.example.bookingmeeting_be.controller.user;

import com.example.bookingmeeting_be.model.Users;
import com.example.bookingmeeting_be.model.dto.BookingRequest;
import com.example.bookingmeeting_be.services.BookingService;
import com.example.bookingmeeting_be.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class UserBookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public String createBooking(@RequestParam Integer roomId,
                                @RequestParam String title,
                                @RequestParam String bookingDate,
                                @RequestParam String startTimeStr,
                                @RequestParam String endTimeStr,
                                @RequestParam(required = false) List<Integer> attendeeUserIds,
                                Authentication auth,
                                RedirectAttributes ra) {
        try {
            Users host = userService.findByEmail(auth.getName());

            LocalDate date = LocalDate.parse(bookingDate);
            LocalDateTime start = LocalDateTime.of(date, LocalTime.parse(startTimeStr));
            LocalDateTime end = LocalDateTime.of(date, LocalTime.parse(endTimeStr));

            BookingRequest request = new BookingRequest();
            request.setRoomId(roomId);
            request.setTitle(title);
            request.setStartTime(start);
            request.setEndTime(end);
            request.setHostUserId(host.getUserId());
            request.setAttendeeUserIds(attendeeUserIds);
            request.setIsHostParticipating(true);

            bookingService.createBooking(request);

            ra.addFlashAttribute("success", "Đặt phòng thành công!");
            return "redirect:/users/history";

        } catch (Exception e) {
            ra.addFlashAttribute("error", "Đặt phòng thất bại: " + e.getMessage());
            return "redirect:/users/rooms/" + roomId;
        }
    }
}
