package com.rentit.controller;

import com.rentit.dto.MessageResponse;
import com.rentit.dto.PropertyDTO;
import com.rentit.dto.UserDTO;
import com.rentit.entity.Booking;
import com.rentit.entity.Property;
import com.rentit.entity.User;
import com.rentit.repository.BookingRepository;
import com.rentit.repository.PropertyRepository;
import com.rentit.repository.ReviewRepository;
import com.rentit.repository.UserRepository;
import com.rentit.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class AdminController {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final PropertyService propertyService;
    private final ModelMapper modelMapper;

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> users = userRepository.findAll(pageable);
        
        List<UserDTO> userDTOs = users.getContent().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("users", userDTOs);
        response.put("currentPage", users.getNumber());
        response.put("totalPages", users.getTotalPages());
        response.put("totalUsers", users.getTotalElements());
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == User.Role.ADMIN) {
            throw new RuntimeException("Cannot delete admin users");
        }

        userRepository.delete(user);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
    }

    @PatchMapping("/users/{id}/role")
    public ResponseEntity<Map<String, Object>> updateUserRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User.Role role = User.Role.valueOf(request.get("role"));
        user.setRole(role);
        User updatedUser = userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User role updated successfully");
        response.put("user", modelMapper.map(updatedUser, UserDTO.class));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/properties/pending")
    public ResponseEntity<Map<String, Object>> getPendingProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Property> properties = propertyRepository.findByIsVerifiedFalseAndDeletedAtIsNull(pageable);
        
        Page<PropertyDTO> propertyDTOs = properties.map(property -> {
            PropertyDTO dto = modelMapper.map(property, PropertyDTO.class);
            dto.setOwner(modelMapper.map(property.getOwner(), UserDTO.class));
            return dto;
        });
        
        Map<String, Object> response = new HashMap<>();
        response.put("properties", propertyDTOs.getContent());
        response.put("currentPage", propertyDTOs.getNumber());
        response.put("totalPages", propertyDTOs.getTotalPages());
        response.put("totalProperties", propertyDTOs.getTotalElements());
        
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/properties/{id}/verify")
    public ResponseEntity<Map<String, Object>> verifyProperty(@PathVariable Long id) {
        Property property = propertyRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        property.setIsVerified(true);
        Property updatedProperty = propertyRepository.save(property);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Property verified successfully");
        response.put("property", modelMapper.map(updatedProperty, PropertyDTO.class));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics() {
        long totalUsers = userRepository.count();
        long totalOwners = userRepository.countByRole(User.Role.OWNER);
        long totalTenants = userRepository.countByRole(User.Role.TENANT);
        
        long totalProperties = propertyRepository.countByDeletedAtIsNull();
        long availableProperties = propertyRepository.countByAvailabilityStatusAndDeletedAtIsNull(Property.AvailabilityStatus.AVAILABLE);
        long rentedProperties = propertyRepository.countByAvailabilityStatusAndDeletedAtIsNull(Property.AvailabilityStatus.RENTED);
        
        long totalBookings = bookingRepository.count();
        long pendingBookings = bookingRepository.countByBookingStatus(Booking.BookingStatus.PENDING);
        long approvedBookings = bookingRepository.countByBookingStatus(Booking.BookingStatus.APPROVED);
        long rejectedBookings = bookingRepository.countByBookingStatus(Booking.BookingStatus.REJECTED);
        long cancelledBookings = bookingRepository.countByBookingStatus(Booking.BookingStatus.CANCELLED);
        
        long totalReviews = reviewRepository.count();
        
        Map<String, Object> users = new HashMap<>();
        users.put("total", totalUsers);
        users.put("owners", totalOwners);
        users.put("tenants", totalTenants);
        
        Map<String, Object> properties = new HashMap<>();
        properties.put("total", totalProperties);
        properties.put("available", availableProperties);
        properties.put("rented", rentedProperties);
        
        Map<String, Object> bookings = new HashMap<>();
        bookings.put("total", totalBookings);
        bookings.put("pending", pendingBookings);
        bookings.put("approved", approvedBookings);
        bookings.put("rejected", rejectedBookings);
        bookings.put("cancelled", cancelledBookings);
        
        Map<String, Object> reviews = new HashMap<>();
        reviews.put("total", totalReviews);
        
        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("properties", properties);
        response.put("bookings", bookings);
        response.put("reviews", reviews);
        
        return ResponseEntity.ok(response);
    }
}
