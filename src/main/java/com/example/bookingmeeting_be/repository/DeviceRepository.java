package com.example.bookingmeeting_be.repository;

import com.example.bookingmeeting_be.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    List<Device> findByType(String type);
    List<Device> findByStatus(String status);
    List<Device> findByStatusAndQuantityGreaterThan(String status, int quantity);

    @Query("SELECT d FROM Device d WHERE " +
            "(:name IS NULL OR :name = '' OR d.name LIKE %:name%) AND " +
            "(:type IS NULL OR :type = '' OR d.type LIKE %:type%) AND " +
            "(:status IS NULL OR :status = '' OR d.status = :status)")
    List<Device> searchDevices(
            @Param("name") String name,
            @Param("type") String type,
            @Param("status") String status);
}

