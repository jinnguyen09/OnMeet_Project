package com.example.bookingmeeting_be.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomUsageDTO {
    private Integer roomId;
    private String roomName;
    private Integer capacity;
    private long totalBookings;
}