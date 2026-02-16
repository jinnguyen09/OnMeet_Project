package com.example.bookingmeeting_be.repository;

import com.example.bookingmeeting_be.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role>  findByName(String name);
}
