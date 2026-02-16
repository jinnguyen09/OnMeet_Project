package com.example.bookingmeeting_be.controller.admin.view;

import com.example.bookingmeeting_be.model.Device;
import com.example.bookingmeeting_be.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/devices")
public class DeviceViewController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public String listDevices(Model model) {
        List<Device> devices = deviceService.getAll();
        model.addAttribute("devices", devices);
        model.addAttribute("device", new Device());
        return "admin/device-list";
    }

    @PostMapping("/add")
    public String addDevice(@ModelAttribute Device device, RedirectAttributes redirectAttributes) {
        deviceService.create(device);
        redirectAttributes.addFlashAttribute("success", "Thêm thiết bị thành công!");
        return "redirect:/admin/devices";
    }


    @GetMapping("/edit/{id}")
    public String editDeviceForm(@PathVariable int id, Model model) {
        Device device = deviceService.getById(id);
        model.addAttribute("device", device);
        return "device-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateDevice(@PathVariable int id, @ModelAttribute Device device,
                               RedirectAttributes redirectAttributes) {
        deviceService.update(id, device);
        redirectAttributes.addFlashAttribute("success", "Cập nhật thiết bị thành công!");
        return "redirect:/admin/devices";
    }

    @GetMapping("/delete/{id}")
    public String deleteDevice(@PathVariable int id, RedirectAttributes redirectAttributes) {
        deviceService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Xóa thiết bị thành công!");
        return "redirect:/admin/devices";

    }
    @GetMapping("/available")
    public String getAvailable(Model model) {
        List<Device> devices = deviceService.getAvailableDevices();
        model.addAttribute("devices", devices);
        model.addAttribute("device", new Device());
        return "admin/devices-available";
    }

    @GetMapping("/search")
    public String searchDevices(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            Model model) {

        List<Device> results = deviceService.search(name, type, status);

        model.addAttribute("devices", results);
        model.addAttribute("device", new Device());

        model.addAttribute("searchName", name);
        model.addAttribute("searchType", type);
        model.addAttribute("searchStatus", status);

        return "admin/device-list";
    }
}