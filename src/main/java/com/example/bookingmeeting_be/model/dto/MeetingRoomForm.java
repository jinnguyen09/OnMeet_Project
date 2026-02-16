package com.example.bookingmeeting_be.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
@Data
public class MeetingRoomForm {
    private Integer id;
    private String name;
    private Integer capacity;
    private String features;
    private String status;
    private String imageUrl;
    private MultipartFile imageFile;
    private Double pricePerHour;
    private String address;
    private List<AssetItem> assetItems = new ArrayList<>();
    @Setter
    @Getter
    public static class AssetItem {
        private Integer deviceId;
        private Integer quantity;

    }

}
