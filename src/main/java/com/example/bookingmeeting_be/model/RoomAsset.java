package com.example.bookingmeeting_be.model;

import jakarta.persistence.*;
import com.example.bookingmeeting_be.model.Device;

@Entity
@Table(name = "room_assets")
public class RoomAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private Integer id;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private MeetingRoom meetingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private com.example.bookingmeeting_be.model.Device device;

    public RoomAsset() {
    }

    public RoomAsset(Integer quantity, MeetingRoom meetingRoom, com.example.bookingmeeting_be.model.Device device) {
        this.quantity = quantity;
        this.meetingRoom = meetingRoom;
        this.device = device;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public MeetingRoom getMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(MeetingRoom meetingRoom) {
        this.meetingRoom = meetingRoom;
    }

    public com.example.bookingmeeting_be.model.Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
