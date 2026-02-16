package com.example.bookingmeeting_be.controller.admin.view;

import com.example.bookingmeeting_be.model.dto.UserResponse;
import com.example.bookingmeeting_be.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminUsersController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listUsers(@RequestParam(required = false) String q,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        Page<UserResponse> userPage = userService.listUsers(q, page, size);
        model.addAttribute("userPage", userPage);
        model.addAttribute("q",q);
        model.addAttribute("page",page);
        model.addAttribute("size",size);
        return "admin/admin-users";
    }
    @PostMapping("/users/{id}/roles")
    public String updateRoles(@PathVariable int id,
                              @RequestParam(required = false) List<String> roles,
                              @ModelAttribute UserResponse userUpdate) {

        userService.updateUserAdmin(id,
                roles != null ? new HashSet<>(roles) : new HashSet<>(),
                userUpdate);

        return "redirect:/admin/users";
    }

    @PostMapping("/users/create")
    public String createUser(@ModelAttribute UserResponse userRequest,
                             @RequestParam(required = false) List<String> roles) {
        userService.createUserAdmin(userRequest, roles != null ? new HashSet<>(roles) : new HashSet<>());

        return "redirect:/admin/users";
    }
}
