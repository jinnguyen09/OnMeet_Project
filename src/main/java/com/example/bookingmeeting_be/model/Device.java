package com.example.bookingmeeting_be.model;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "devices")
//public class Device {
//    @Id
//    @Column(name = "device_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    private String name;
//    private String category;
//    private String status;
//    private Integer quantity;
//    private String description;
//
//    public Device(int id, String name, String category, String status, Integer quantity, String description) {
//        this.id = id;
//        this.name = name;
//        this.category = category;
//        this.status = status;
//        this.quantity = quantity;
//        this.description = description;
//    }
//
//    public Device() {
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public Integer getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(Integer quantity) {
//        this.quantity = quantity;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//}


//import com.example.bookingmeeting_be.model.RoomAsset;
//import jakarta.persistence.*;
//import java.util.HashSet;
//import java.util.Set;
//
//@Entity
//@Table(name = "devices")
//public class Device {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "device_id")
//    private int id;
//
//    @Column(name = "name", nullable = false)
//    private String name;
//
//    @Column(name = "description")
//    private String description;
//
//    @Column(name = "type")
//    private String type;
//
//    @Column(name = "status")
//    private String status;
//    @Column(name = "quantity")
//    private int quantity;
//
//    // Quan hệ 1-n với RoomAsset
//    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<RoomAsset> roomAssets = new HashSet<>();
//
//    public Device() {}
//
//    public Device(String name, String description, String type, String status, int quantity) {
//        this.name = name;
//        this.description = description;
//        this.type = type;
//        this.status = status;
//        this.quantity = quantity;
//    }
//
//    // Getter và Setter
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    public Set<RoomAsset> getRoomAssets() {
//        return roomAssets;
//    }
//
//    public void setRoomAssets(Set<RoomAsset> roomAssets) {
//        this.roomAssets = roomAssets;
//    }
//}


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "quantity")
    private int quantity;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<RoomAsset> roomAssets = new HashSet<>();
}