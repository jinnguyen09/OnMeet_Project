package com.example.bookingmeeting_be.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer id;

    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "host_user_id")
    private Integer hostUserId;

    @Column(name = "title")
    private String title;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "total_price")
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private MeetingRoom room;

    @ManyToOne
    @JoinColumn(name = "host_user_id", insertable = false, updatable = false)
    private Users host;
}