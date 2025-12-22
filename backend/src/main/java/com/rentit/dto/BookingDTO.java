package com.rentit.dto;

import com.rentit.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private PropertyDTO property;
    private UserDTO tenant;
    private UserDTO owner;
    private Booking.BookingStatus bookingStatus;
    private LocalDate checkInDate;
    private LocalDateTime bookingDate;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
