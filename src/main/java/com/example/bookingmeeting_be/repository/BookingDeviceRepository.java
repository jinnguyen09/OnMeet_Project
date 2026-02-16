package com.example.bookingmeeting_be.repository;

import com.example.bookingmeeting_be.model.BookingDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface BookingDeviceRepository extends JpaRepository<BookingDevice, Integer> {
    List<BookingDevice> findByBookingId(Integer bookingId);

    @Modifying
    @Transactional
    @Query("DELETE FROM BookingDevice bd WHERE bd.booking.id = :bookingId")
    void deleteByBookingId(Integer bookingId);
}