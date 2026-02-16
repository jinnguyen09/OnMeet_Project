package com.example.bookingmeeting_be.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_recipients")
@Data
@NoArgsConstructor
public class NotificationRecipient {

    @EmbeddedId
    private NotificationRecipientId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("notiId")
    @JoinColumn(name = "noti_id")
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "is_read")
    private Boolean isRead = false;
}
