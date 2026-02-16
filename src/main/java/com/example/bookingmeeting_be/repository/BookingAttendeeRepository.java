package com.example.bookingmeeting_be.repository;

import com.example.bookingmeeting_be.model.Booking;
import com.example.bookingmeeting_be.model.BookingAttendee;
import com.example.bookingmeeting_be.model.BookingAttendeeId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface BookingAttendeeRepository
        extends JpaRepository<BookingAttendee, BookingAttendeeId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM BookingAttendee ba WHERE ba.booking.id = :bookingId")
    void deleteByBookingAttendeeIdBookingId(Integer bookingId);

    List<BookingAttendee> findByBookingAttendeeIdBookingId(Integer bookingId);

    Optional<BookingAttendee>
    findByBookingAttendeeIdBookingIdAndBookingAttendeeIdUserId(Integer bookingId, Integer userId);

    long countByBookingAttendeeIdBookingIdAndStatus(Integer bookingId, String status);

    List<BookingAttendee> findByUsers_UserId(Integer userId);

    @Query("""
              select ba.bookingAttendeeId.bookingId, count(ba)
              from BookingAttendee ba
              where ba.status = 'ACCEPTED'
              group by ba.bookingAttendeeId.bookingId
            """)
    List<Object[]> countAcceptedGroupByBooking();


    @Query("""
              select ba.bookingAttendeeId.bookingId, count(ba)
              from BookingAttendee ba
              group by ba.bookingAttendeeId.bookingId
            """)
    List<Object[]> countAllGroupByBooking();

    @Modifying
    @Query("""
              update BookingAttendee ba
               set ba.status = :status
               where ba.bookingAttendeeId.bookingId = :bookingId
               and ba.bookingAttendeeId.userId = :userId
                and ba.status = 'INVITED'
            """)
    int updateStatus(@Param("bookingId") Integer bookingId,
                     @Param("userId") Integer userId,
                     @Param("status") String status);
    @Query("""
   select ba.bookingAttendeeId.bookingId, ba.status
   from BookingAttendee ba
   where ba.bookingAttendeeId.userId = :userId
     and ba.bookingAttendeeId.bookingId in :bookingIds
""")
    List<Object[]> findStatusesByUserAndBookingIds(@Param("userId") Integer userId,
                                                   @Param("bookingIds") List<Integer> bookingIds);

    @Query("""
    select count(distinct ba.users.userId)
    from BookingAttendee ba
    join ba.booking b
    where b.status <> :cancelled
      and b.startTime >= :from and b.startTime < :to
""")
    long countDistinctActiveParticipants(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("cancelled") String cancelled
    );
}

