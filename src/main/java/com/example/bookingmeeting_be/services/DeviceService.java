package com.example.bookingmeeting_be.services;

import com.example.bookingmeeting_be.model.Device;
import com.example.bookingmeeting_be.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository repository;

    @Autowired
    private DeviceRepository deviceRepository;

    public List<Device> getAll() {
        return repository.findAll();
    }
    public Device create(Device device) {
        return repository.save(device);
    }
    public Device update(int id, Device device) {
        device.setId(id);
        return repository.save(device);
    }
    public void delete(int id) {
        repository.deleteById(id);
    }
    public void updateQuantity(int id, int delta) {
        Device d = repository.findById(id).orElseThrow();
        d.setQuantity(d.getQuantity() + delta);
        repository.save(d);
    }

    public Device getById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thiết bị với ID: " + id));
    }
    public List<Device> getAvailableDevices() {
        return repository.findByStatusAndQuantityGreaterThan("AVAILABLE", 0);
    }

    public List<Device> search(String name, String type, String status) {
        return deviceRepository.searchDevices(name, type, status);
    }

}