package com.example.bookingmeeting_be.services;

import com.example.bookingmeeting_be.model.Booking;
import com.example.bookingmeeting_be.model.dto.DashboardStats;
import com.example.bookingmeeting_be.model.dto.RecentMeetingDTO;
import com.example.bookingmeeting_be.model.dto.RoomUsageDTO;
import com.example.bookingmeeting_be.repository.BookingAttendeeRepository;
import com.example.bookingmeeting_be.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final BookingRepository bookingRepository;
    private final BookingAttendeeRepository bookingAttendeeRepository;

    private static final String CANCELLED = "CANCELLED";

    public DashboardStats getDashboardStats() {

        // ===== 1) Mốc thời gian =====
        LocalDate today = LocalDate.now();
        LocalDateTime startToday = today.atStartOfDay();
        LocalDateTime startTomorrow = today.plusDays(1).atStartOfDay();
        LocalDateTime startYesterday = today.minusDays(1).atStartOfDay();

        YearMonth ym = YearMonth.from(today);
        LocalDateTime startThisMonth = ym.atDay(1).atStartOfDay();
        LocalDateTime startNextMonth = ym.plusMonths(1).atDay(1).atStartOfDay();

        YearMonth lastYm = ym.minusMonths(1);
        LocalDateTime startLastMonth = lastYm.atDay(1).atStartOfDay();

        // ===== 2) Cards =====
        long meetingsToday =
                bookingRepository.countByStartTimeGreaterThanEqualAndStartTimeLessThanAndStatusNot(
                        startToday, startTomorrow, CANCELLED
                );

        long meetingsYesterday =
                bookingRepository.countByStartTimeGreaterThanEqualAndStartTimeLessThanAndStatusNot(
                        startYesterday, startToday, CANCELLED
                );

        long meetingsThisMonth =
                bookingRepository.countByStartTimeGreaterThanEqualAndStartTimeLessThanAndStatusNot(
                        startThisMonth, startNextMonth, CANCELLED
                );

        long meetingsLastMonth =
                bookingRepository.countByStartTimeGreaterThanEqualAndStartTimeLessThanAndStatusNot(
                        startLastMonth, startThisMonth, CANCELLED
                );

        long deltaVsYesterday = meetingsToday - meetingsYesterday;

        double growthPct;
        if (meetingsLastMonth == 0) {
            growthPct = (meetingsThisMonth > 0) ? 100.0 : 0.0;
        } else {
            growthPct = ((double) (meetingsThisMonth - meetingsLastMonth) * 100.0) / meetingsLastMonth;
        }

        // Người tham gia “hoạt động” (ví dụ theo tháng này)
        long activeParticipants = bookingAttendeeRepository.countDistinctActiveParticipants(
                startThisMonth, startNextMonth, CANCELLED
        );

        // ===== 3) Bảng lịch họp gần đây =====
        List<RecentMeetingDTO> recentMeetings =
                bookingRepository.findRecentMeetingRows(CANCELLED, PageRequest.of(0, 5));

        // set uiStatus cho badge
        LocalDateTime now = LocalDateTime.now();
        for (RecentMeetingDTO r : recentMeetings) {
            String uiStatus = now.isBefore(r.getStartTime()) ? "UPCOMING"
                    : now.isAfter(r.getEndTime()) ? "DONE"
                    : "ONGOING";
            r.setUiStatus(uiStatus);
        }

        // ===== 4) Top rooms =====
        List<RoomUsageDTO> topRooms =
                bookingRepository.findTopRoomRows(startThisMonth, startNextMonth, CANCELLED, PageRequest.of(0, 5));

        // ===== 5) Chart: hôm nay theo giờ =====
        List<Booking> todayBookings =
                bookingRepository.findByStartTimeGreaterThanEqualAndStartTimeLessThanAndStatusNot(
                        startToday, startTomorrow, CANCELLED
                );

        long[] bucket = new long[24];
        for (Booking b : todayBookings) {
            int hour = b.getStartTime().getHour();
            if (hour >= 0 && hour < 24) bucket[hour]++;
        }

        List<String> chartLabels = new ArrayList<>();
        List<Long> chartValues = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            chartLabels.add(String.format("%02d:00", h));
            chartValues.add(bucket[h]);
        }

        // ===== 6) Build DashboardStats =====
        DashboardStats stats = new DashboardStats();
        stats.setMeetingsToday(meetingsToday);
        stats.setDeltaVsYesterday(deltaVsYesterday);

        stats.setMeetingsThisMonth(meetingsThisMonth);
        stats.setGrowthPct(growthPct);

        stats.setActiveParticipants(activeParticipants);

        stats.setRecentMeetings(recentMeetings);
        stats.setTopRooms(topRooms);

        stats.setChartLabels(chartLabels);
        stats.setChartValues(chartValues);

        return stats;
    }
}
