package com.example.bookingmeeting_be.model.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStats {

    private long meetingsToday;
    private long deltaVsYesterday;

    private long meetingsThisMonth;
    private double growthPct;

    private long activeParticipants;

    private List<RecentMeetingDTO> recentMeetings;

    private List<RoomUsageDTO> topRooms;

    private List<String> chartLabels;
    private List<Long> chartValues;
}