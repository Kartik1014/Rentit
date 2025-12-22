package com.rentit.service;

import com.rentit.dto.BookingDTO;
import com.rentit.dto.BookingRequest;
import com.rentit.dto.MessageResponse;
import com.rentit.entity.Booking;
import com.rentit.entity.Property;
import com.rentit.entity.User;
import com.rentit.exception.ResourceNotFoundException;
import com.rentit.repository.BookingRepository;
import com.rentit.repository.PropertyRepository;
import com.rentit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public BookingDTO createBooking(BookingRequest request, String userEmail) {
        User tenant = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Property property = propertyRepository.findByIdAndDeletedAtIsNull(request.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        if (property.getAvailabilityStatus() != Property.AvailabilityStatus.AVAILABLE) {
            throw new RuntimeException("Property is not available for booking");
        }

        if (property.getOwner().getId().equals(tenant.getId())) {
            throw new RuntimeException("You cannot book your own property");
        }

        // Check for existing active booking
        bookingRepository.findByPropertyIdAndTenantIdAndBookingStatusIn(
                property.getId(),
                tenant.getId(),
                Arrays.asList(Booking.BookingStatus.PENDING, Booking.BookingStatus.APPROVED)
        ).ifPresent(b -> {
            throw new RuntimeException("You already have an active booking for this property");
        });

        Booking booking = Booking.builder()
                .property(property)
                .tenant(tenant)
                .owner(property.getOwner())
                .checkInDate(request.getCheckInDate())
                .notes(request.getNotes())
                .bookingStatus(Booking.BookingStatus.PENDING)
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        return mapToDTO(savedBooking);
    }

    public BookingDTO getBookingById(Long id, String userEmail) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!booking.getTenant().getId().equals(user.getId()) &&
            !booking.getOwner().getId().equals(user.getId()) &&
            user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to view this booking");
        }

        return mapToDTO(booking);
    }

    @Transactional
    public BookingDTO approveBooking(Long id, String userEmail) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!booking.getOwner().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to approve this booking");
        }

        if (booking.getBookingStatus() != Booking.BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not in pending status");
        }

        booking.setBookingStatus(Booking.BookingStatus.APPROVED);
        Booking savedBooking = bookingRepository.save(booking);

        // Update property status
        Property property = booking.getProperty();
        property.setAvailabilityStatus(Property.AvailabilityStatus.RENTED);
        propertyRepository.save(property);

        return mapToDTO(savedBooking);
    }

    @Transactional
    public BookingDTO rejectBooking(Long id, String userEmail) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!booking.getOwner().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to reject this booking");
        }

        if (booking.getBookingStatus() != Booking.BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not in pending status");
        }

        booking.setBookingStatus(Booking.BookingStatus.REJECTED);
        return mapToDTO(bookingRepository.save(booking));
    }

    @Transactional
    public BookingDTO cancelBooking(Long id, String userEmail) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!booking.getTenant().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to cancel this booking");
        }

        if (booking.getBookingStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        // If booking was approved, revert property status
        if (booking.getBookingStatus() == Booking.BookingStatus.APPROVED) {
            Property property = booking.getProperty();
            property.setAvailabilityStatus(Property.AvailabilityStatus.AVAILABLE);
            propertyRepository.save(property);
        }

        booking.setBookingStatus(Booking.BookingStatus.CANCELLED);
        return mapToDTO(bookingRepository.save(booking));
    }

    public Page<BookingDTO> getTenantBookings(Long tenantId, Pageable pageable, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getId().equals(tenantId) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to view these bookings");
        }

        Page<Booking> bookings = bookingRepository.findByTenantId(tenantId, pageable);
        return bookings.map(this::mapToDTO);
    }

    public Page<BookingDTO> getOwnerBookings(Long ownerId, Pageable pageable, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getId().equals(ownerId) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to view these bookings");
        }

        Page<Booking> bookings = bookingRepository.findByOwnerId(ownerId, pageable);
        return bookings.map(this::mapToDTO);
    }

    private BookingDTO mapToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setBookingStatus(booking.getBookingStatus());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setBookingDate(booking.getBookingDate());
        dto.setNotes(booking.getNotes());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUpdatedAt(booking.getUpdatedAt());
        
        // Map simplified property info
        Property property = booking.getProperty();
        dto.setProperty(modelMapper.map(property, com.rentit.dto.PropertyDTO.class));
        
        dto.setTenant(modelMapper.map(booking.getTenant(), com.rentit.dto.UserDTO.class));
        dto.setOwner(modelMapper.map(booking.getOwner(), com.rentit.dto.UserDTO.class));
        
        return dto;
    }
}
