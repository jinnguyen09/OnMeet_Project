package com.example.bookingmeeting_be.repository;

import com.example.bookingmeeting_be.model.RoomAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomAssetRepository extends JpaRepository<RoomAsset, Integer> {
    List<RoomAsset> findByMeetingRoom_Id(Long roomId);
    void deleteByMeetingRoom_Id(Long roomId);
}
