package com.example.bookingmeeting_be.repository;

import com.example.bookingmeeting_be.model.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Integer> {
    @Query("""
        SELECT DISTINCT r
        FROM MeetingRoom r
        LEFT JOIN FETCH r.roomAssets ra
        LEFT JOIN FETCH ra.device
    """)
    List<MeetingRoom> findAllWithAssets();

    @Query("SELECT DISTINCT m FROM MeetingRoom m LEFT JOIN FETCH m.roomAssets " +
            "WHERE (:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:address IS NULL OR LOWER(m.address) LIKE LOWER(CONCAT('%', :address, '%'))) " +
            "AND (:capacity IS NULL OR m.capacity >= :capacity) " +
            "AND (:status IS NULL OR m.status = :status)")
    List<MeetingRoom> searchRooms(@Param("name") String name,
                                  @Param("address") String address,
                                  @Param("capacity") Integer capacity,
                                  @Param("status") String status);

}
