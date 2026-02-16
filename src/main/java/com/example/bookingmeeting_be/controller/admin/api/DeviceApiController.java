package com.example.bookingmeeting_be.controller.admin.api;

import com.example.bookingmeeting_be.model.Device;
import com.example.bookingmeeting_be.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "*")
public class DeviceApiController {

    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping("/available")
    public List<Device> getAvailableDevices() {
        return deviceRepository.findByStatusAndQuantityGreaterThan("Available", 0);
    }
}