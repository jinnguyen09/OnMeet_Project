package com.example.bookingmeeting_be.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentMeetingDTO {
    private Integer bookingId;
    private String hostName;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // cho UI badge: UPCOMING / ONGOING / DONE
    private String uiStatus;
}