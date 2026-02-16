package com.example.bookingmeeting_be.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "Meeting_Rooms")
public class MeetingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "features")
    private String features;

    @Column(name = "status")
    private String status;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "price_per_hour")
    private Double pricePerHour;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "meetingRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomAsset> roomAssets = new HashSet<>();

    public MeetingRoom() {
    }

    public MeetingRoom(String name, Integer capacity, String features, String status, String imageUrl, Double pricePerHour, String address) {
        this.name = name;
        this.capacity = capacity;
        this.features = features;
        this.status = status;
        this.imageUrl = imageUrl;
        this.pricePerHour = pricePerHour;
        this.address = address;
    }
}
