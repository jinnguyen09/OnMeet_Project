package com.example.bookingmeeting_be.controller.user;

import com.example.bookingmeeting_be.model.MeetingRoom;
import com.example.bookingmeeting_be.services.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller(value = "UserMeetingRoomController")
@RequestMapping("/users")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private com.example.bookingmeeting_be.services.UserService userService;

    @Autowired
    private com.example.bookingmeeting_be.services.BookingService bookingService;

    @GetMapping("/meeting-rooms")
    public String listRooms(Model model) {
        List<MeetingRoom> rooms = meetingRoomService.getAllWithAssets();
        model.addAttribute("rooms", rooms);
        model.addAttribute("activePage", "meeting-rooms");
        return "users/meeting-rooms";
    }

    @GetMapping("/rooms/{id}")
    public String getRoomDetail(@PathVariable Integer id, Model model) {
        MeetingRoom room = meetingRoomService.getById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng với ID: " + id));

        model.addAttribute("room", room);
        return "users/meeting-room-detail";
    }

    @GetMapping("/api/users/search")
    @ResponseBody
    public List<com.example.bookingmeeting_be.model.dto.UserResponse> searchUsers(@org.springframework.web.bind.annotation.RequestParam String q) {
        return userService.listUsers(q, 0, 10).getContent();
    }

    @org.springframework.web.bind.annotation.PostMapping("/bookings/create")
    public String createBooking(@org.springframework.web.bind.annotation.ModelAttribute com.example.bookingmeeting_be.model.dto.BookingRequest bookingRequest,
                                org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            bookingService.createBooking(bookingRequest);
            redirectAttributes.addFlashAttribute("success", "Gửi yêu cầu đặt phòng thành công! Vui lòng chờ quản trị viên duyệt.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/users/rooms/" + bookingRequest.getRoomId();
    }
}