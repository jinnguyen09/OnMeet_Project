package com.example.bookingmeeting_be.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_id")
    private Integer notificationId;
    @Column(name = "booking_id")
    private Integer bookingId;


    private String type;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;

}
