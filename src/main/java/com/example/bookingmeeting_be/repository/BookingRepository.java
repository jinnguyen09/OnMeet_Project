package com.example.bookingmeeting_be.repository;

import com.example.bookingmeeting_be.model.Booking;
import com.example.bookingmeeting_be.model.dto.RecentMeetingDTO;
import com.example.bookingmeeting_be.model.dto.RoomUsageDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByHostUserIdOrderByStartTimeDesc(Integer hostUserId);
    @Query ("""
        SELECT COUNT(b) > 0
        FROM Booking b
        WHERE b.roomId = :roomId
          AND b.status <> 'CANCELLED'
          AND (
              :startTime < b.endTime
              AND :endTime > b.startTime
          )
    """)
    boolean existsOverlappingBooking(@Param("roomId") Integer roomId,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime")  LocalDateTime endTime);
    @Query("""
    SELECT COUNT(b) > 0
    FROM Booking b
    WHERE b.roomId = :roomId
      AND b.id <> :bookingId
      AND b.status <> 'CANCELLED'
      AND :start < b.endTime
      AND :end > b.startTime
""")
    boolean existsOverlappingBookingForUpdate(
            @Param("bookingId") Integer bookingId,
            @Param("roomId") Integer roomId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
            SELECT b FROM Booking b 
            JOIN BookingAttendee ba ON b.id = ba.booking.id WHERE ba.users.userId = :userId
            ORDER BY b.startTime DESC
            """)
    List<Booking> findBookingsByAttendee(@Param("userId") Integer userId);

    //dashboardStat

    @Query("""
    select new com.example.bookingmeeting_be.model.dto.RecentMeetingDTO(
        b.id,
        u.fullname,
        b.title,
        b.startTime,
        b.endTime,
        ''
    )
    from Booking b, Users u
    where u.userId = b.hostUserId
      and b.status <> :CANCELLED
    order by b.startTime desc
""")
    List<RecentMeetingDTO> findRecentMeetingRows(
            @Param("CANCELLED") String CANCELLED,
            Pageable pageable
    );

    @Query("""
        select new com.example.bookingmeeting_be.model.dto.RoomUsageDTO(
            mr.id,
            mr.name,
            mr.capacity,
            count(b)
        )
        from Booking b
        join MeetingRoom mr on mr.id = b.roomId
        where b.status <> :CANCELLED
          and b.startTime >= :from and b.startTime < :to
        group by mr.id, mr.name, mr.capacity
        order by count(b) desc
    """)
    List<RoomUsageDTO> findTopRoomRows(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("CANCELLED") String CANCELLED,
            Pageable pageable
    );

    long countByStartTimeGreaterThanEqualAndStartTimeLessThanAndStatusNot(
            LocalDateTime from, LocalDateTime to, String cancelledStatus);

    List<Booking> findByStartTimeGreaterThanEqualAndStartTimeLessThanAndStatusNot(
            LocalDateTime from, LocalDateTime to, String cancelledStatus);

    long countByStatus(String status);

    List<Booking> findByStatus(String status);
}
