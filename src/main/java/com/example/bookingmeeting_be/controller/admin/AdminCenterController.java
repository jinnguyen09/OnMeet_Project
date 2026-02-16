package com.example.bookingmeeting_be.controller.admin;

import com.example.bookingmeeting_be.model.Booking;
import com.example.bookingmeeting_be.repository.BookingRepository;
import com.example.bookingmeeting_be.repository.DeviceRepository;
import com.example.bookingmeeting_be.repository.MeetingRoomRepository;
import com.example.bookingmeeting_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/admin")
@Controller
public class AdminCenterController {
    @Autowired
    private MeetingRoomRepository roomRepo;

    @Autowired
    private DeviceRepository deviceRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @GetMapping("/admin-center")
    public String homeAdmin(Model model){
        long totalRooms = roomRepo.count();
        long totalDevices = deviceRepo.count();
        long totalUsers = userRepo.count();
        long activeBookings = bookingRepo.countByStatus("Booked");

        List<Booking> pendingList = bookingRepo.findByStatus("PENDING");

        model.addAttribute("pendingBookings", pendingList);
        model.addAttribute("pendingNotifications", pendingList);
        model.addAttribute("pendingCount", pendingList.size());

        Map<Integer, String> roomNames = new HashMap<>();
        roomRepo.findAll().forEach(r -> roomNames.put(r.getId(), r.getName()));
        model.addAttribute("roomNames", roomNames);

        Map<Integer, String> hostNames = new HashMap<>();
        userRepo.findAll().forEach(u -> hostNames.put(u.getUserId(), u.getFullname()));
        model.addAttribute("hostNames", hostNames);

        model.addAttribute("totalRooms", totalRooms);
        model.addAttribute("totalDevices", totalDevices);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeBookings", activeBookings);

        return "admin/admin-center";
    }
}
