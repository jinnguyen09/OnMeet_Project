package com.example.bookingmeeting_be.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDepartmentController {
    @GetMapping("/admin/admin-department")
    public String phongHop(){
        return "admin/admin-department";
    }
}
