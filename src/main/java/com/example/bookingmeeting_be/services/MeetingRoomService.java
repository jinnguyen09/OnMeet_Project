package com.example.bookingmeeting_be.services;

import com.example.bookingmeeting_be.model.Device;
import com.example.bookingmeeting_be.model.MeetingRoom;
import com.example.bookingmeeting_be.model.RoomAsset;
import com.example.bookingmeeting_be.model.dto.MeetingRoomForm;
import com.example.bookingmeeting_be.repository.DeviceRepository;
import com.example.bookingmeeting_be.repository.MeetingRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class MeetingRoomService {

    @Autowired
    private MeetingRoomRepository repository;

    @Autowired
    private DeviceRepository deviceRepository;

    public List<MeetingRoom> getAll() {
        return repository.findAll();
    }

    public List<MeetingRoom> getAllWithAssets() {
        return repository.findAllWithAssets();
    }

    public Optional<MeetingRoom> getById(Integer id) {
        return repository.findById(id);
    }

    public MeetingRoomForm buildFormForEdit(Integer roomId) {
        MeetingRoom room = repository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng họp ID = " + roomId));

        MeetingRoomForm form = new MeetingRoomForm();

        form.setId(room.getId());
        form.setName(room.getName());
        form.setCapacity(room.getCapacity());
        form.setFeatures(room.getFeatures());
        form.setStatus(room.getStatus());
        form.setImageUrl(room.getImageUrl());
        form.setPricePerHour(room.getPricePerHour());
        form.setAddress(room.getAddress());

        if (room.getRoomAssets() != null) {
            room.getRoomAssets().forEach(asset -> {
                MeetingRoomForm.AssetItem item = new MeetingRoomForm.AssetItem();
                item.setDeviceId(asset.getDevice().getId());
                item.setQuantity(asset.getQuantity());
                form.getAssetItems().add(item);
            });
        }

        return form;
    }

    @Transactional
    public MeetingRoom saveFromForm(MeetingRoomForm form) {

        MeetingRoom room = (form.getId() != null)
                ? repository.findById(form.getId())
                .orElseThrow(() -> new RuntimeException("Room not found: " + form.getId()))
                : new MeetingRoom();

        room.setName(form.getName());
        room.setCapacity(form.getCapacity());
        room.setFeatures(form.getFeatures());
        room.setStatus(form.getStatus());
        room.setImageUrl(form.getImageUrl());
        room.setPricePerHour(form.getPricePerHour());
        room.setAddress(form.getAddress());
        room.getRoomAssets().clear();

        if (form.getAssetItems() != null) {
            for (MeetingRoomForm.AssetItem assetItem : form.getAssetItems()) {

                if (assetItem == null) continue;

                if (assetItem.getDeviceId() == null) continue;
                if (assetItem.getQuantity() == null || assetItem.getQuantity() <= 0) continue;

                Device device = deviceRepository.findById(assetItem.getDeviceId())
                        .orElseThrow(() -> new RuntimeException("Device not found: " + assetItem.getDeviceId()));

                RoomAsset ra = new RoomAsset();
                ra.setMeetingRoom(room);
                ra.setDevice(device);
                ra.setQuantity(assetItem.getQuantity());

                room.getRoomAssets().add(ra);
            }
        }

        return repository.save(room);
    }

    public void deletePhysicalFile(String fileName) {
        if (fileName == null || fileName.equals("default-room.jpg")) return;

        try {
            Path filePath = Paths.get("./user-photos/").resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Không thể xóa file: " + fileName + ". Lỗi: " + e.getMessage());
        }
    }

    public List<MeetingRoom> search(String name, String address, Integer capacity, String status) {
        String searchName = (name != null && !name.isBlank()) ? name : null;
        String searchAddress = (address != null && !address.isBlank()) ? address : null;
        String searchStatus = (status != null && !status.isBlank()) ? status : null;

        return repository.searchRooms(searchName, searchAddress, capacity, searchStatus);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
