package com.example.bookingmeeting_be.repository;

import com.example.bookingmeeting_be.model.NotificationRecipient;
import com.example.bookingmeeting_be.model.NotificationRecipientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRecipientRepository
        extends JpaRepository<NotificationRecipient, NotificationRecipientId> {

    // 1) Đếm số thông báo chưa đọc của 1 user
    long countByIdUserIdAndIsReadFalse(Integer userId);

    // 2) Lấy inbox của user (mới nhất trước) + fetch Notification để lấy content/type/createdAt
    @Query("""
        select nr
        from NotificationRecipient nr
        join fetch nr.notification n
        where nr.id.userId = :userId
        order by n.createdAt desc
    """)
    List<NotificationRecipient> findInbox(@Param("userId") Integer userId);

    // 3) Lấy inbox chưa đọc (nếu cần)
    @Query("""
        select nr
        from NotificationRecipient nr
        join fetch nr.notification n
        where nr.id.userId = :userId
          and nr.isRead = false
        order by n.createdAt desc
    """)
    List<NotificationRecipient> findUnreadInbox(@Param("userId") Integer userId);

    // 4) Mark 1 thông báo là đã đọc (theo notiId + userId)
    @Modifying
    @Query("""
        update NotificationRecipient nr
        set nr.isRead = true
        where nr.id.userId = :userId
          and nr.id.notiId = :notiId
    """)
    int markRead(@Param("userId") Integer userId, @Param("notiId") Integer notiId);

    // 5) Mark tất cả là đã đọc
    @Modifying
    @Query("""
        update NotificationRecipient nr
        set nr.isRead = true
        where nr.id.userId = :userId
          and nr.isRead = false
    """)
    int markAllRead(@Param("userId") Integer userId);
}
