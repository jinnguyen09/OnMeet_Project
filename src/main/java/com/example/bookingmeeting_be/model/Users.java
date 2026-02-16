package com.example.bookingmeeting_be.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Entity
@Table(name = "users")
public class Users {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Setter
    @Column(nullable = false, length = 100)
    private String fullname;

    @Setter
    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(name = "phone", length = 20)
    private String phone;

    @Setter
    @Column(name = "address")
    private String address;

    @Setter
    @Column(name = "company")
    private String company;

    @Setter
    @Column(name = "job_title")
    private String jobTitle;

    @Setter
    @Column(name = "about", columnDefinition = "TEXT")
    private String about;

    @Setter
    @Column(name = "avatar")
    private String avatar;

    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        this.createdAt = LocalDateTime.now();
    }

    @Transient
    public String getAvatarPath() {
        if (avatar == null || userId == 0) return "/assets/img/default-avatar.png";
        return "/user-photos/" + userId + "/" + avatar;
    }

}