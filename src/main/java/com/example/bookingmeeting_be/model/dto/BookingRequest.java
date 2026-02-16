package com.example.bookingmeeting_be.model.dto;

import com.example.bookingmeeting_be.model.BookingAttendee;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

//@Data
//public class BookingRequest {
//    private String title;
//    private Integer roomId;
//    private Integer hostUserId;
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;
//    private String description;
//
//    private List<DeviceRequest> devices;
//
//    @Data
//    public static class DeviceRequest {
//        private Integer deviceId;
//        private int quantity;
//    }
//}

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private String title;
    private Integer roomId;
    private Integer hostUserId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;

    private String description;

    private List<DeviceRequest> devices = new java.util.ArrayList<>();
    private List<Integer> attendeeUserIds = new java.util.ArrayList<>();

    private Boolean isHostParticipating = true;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceRequest {
        private Integer deviceId;
        private int quantity;
    }
}