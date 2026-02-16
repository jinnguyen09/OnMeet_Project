    package com.example.bookingmeeting_be.repository;

    import com.example.bookingmeeting_be.model.Notification;
    import org.springframework.data.jpa.repository.JpaRepository;

    import java.util.List;

    public interface NotificationRepository extends JpaRepository<Notification,Integer> {
        List<Notification> findByBookingIdOrderByCreatedAtDesc(Integer bookingId);
    }
