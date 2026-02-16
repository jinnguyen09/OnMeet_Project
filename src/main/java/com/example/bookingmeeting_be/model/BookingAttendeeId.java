package com.example.bookingmeeting_be.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingAttendeeId implements Serializable
{
    private Integer bookingId;
    private Integer userId;
}
