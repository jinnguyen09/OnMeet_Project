package com.example.bookingmeeting_be.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booking_devices")
@Data
@NoArgsConstructor
public class BookingDevice {

    @EmbeddedId
    private BookingDeviceKey id;

    @ManyToOne
    @MapsId("bookingId")
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @MapsId("deviceId")
    @JoinColumn(name = "device_id")
    private Device device;

    @Column(name = "quantity")
    private int quantity;

    public BookingDevice(Booking booking, Device device, int quantity) {
        this.booking = booking;
        this.device = device;
        this.quantity = quantity;
        this.id = new BookingDeviceKey(booking.getId(), device.getId());
    }
}