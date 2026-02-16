package com.example.bookingmeeting_be.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDeviceKey implements Serializable {
    @Column(name = "booking_id")
    private Integer bookingId;

    @Column(name = "device_id")
    private Integer deviceId;
}