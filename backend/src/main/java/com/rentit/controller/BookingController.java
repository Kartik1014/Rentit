package com.rentit.controller;

import com.rentit.dto.BookingDTO;
import com.rentit.dto.BookingRequest;
import com.rentit.dto.MessageResponse;
import com.rentit.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<Map<String, Object>> createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication
    ) {
        BookingDTO booking = bookingService.createBooking(request, authentication.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Booking request created successfully");
        response.put("booking", booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBookingById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        BookingDTO booking = bookingService.getBookingById(id, authentication.getName());
        return ResponseEntity.ok(Map.of("booking", booking));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> approveBooking(
            @PathVariable Long id,
            Authentication authentication
    ) {
        BookingDTO booking = bookingService.approveBooking(id, authentication.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Booking approved successfully");
        response.put("booking", booking);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> rejectBooking(
            @PathVariable Long id,
            Authentication authentication
    ) {
        BookingDTO booking = bookingService.rejectBooking(id, authentication.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Booking rejected");
        response.put("booking", booking);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('TENANT', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> cancelBooking(
            @PathVariable Long id,
            Authentication authentication
    ) {
        BookingDTO booking = bookingService.cancelBooking(id, authentication.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Booking cancelled successfully");
        response.put("booking", booking);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<Map<String, Object>> getTenantBookings(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BookingDTO> bookings = bookingService.getTenantBookings(tenantId, pageable, authentication.getName());
        
        Map<String, Object> response = new HashMap<>();
        response.put("bookings", bookings.getContent());
        response.put("currentPage", bookings.getNumber());
        response.put("totalPages", bookings.getTotalPages());
        response.put("totalBookings", bookings.getTotalElements());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getOwnerBookings(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BookingDTO> bookings = bookingService.getOwnerBookings(ownerId, pageable, authentication.getName());
        
        Map<String, Object> response = new HashMap<>();
        response.put("bookings", bookings.getContent());
        response.put("currentPage", bookings.getNumber());
        response.put("totalPages", bookings.getTotalPages());
        response.put("totalBookings", bookings.getTotalElements());
        
        return ResponseEntity.ok(response);
    }
}
