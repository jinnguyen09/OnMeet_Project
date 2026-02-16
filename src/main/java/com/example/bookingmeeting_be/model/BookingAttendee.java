package com.example.bookingmeeting_be.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booking_attendees")
@Data
@NoArgsConstructor
public class BookingAttendee {
    @EmbeddedId
    private BookingAttendeeId bookingAttendeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookingId")
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private Users users;

    @Column(name = "status")
    private String status;
}
