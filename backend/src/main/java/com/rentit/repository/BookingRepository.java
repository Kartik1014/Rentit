package com.rentit.repository;

import com.rentit.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    Page<Booking> findByTenantId(Long tenantId, Pageable pageable);
    
    Page<Booking> findByOwnerId(Long ownerId, Pageable pageable);
    
    Optional<Booking> findByPropertyIdAndTenantIdAndBookingStatusIn(
        Long propertyId, 
        Long tenantId, 
        java.util.List<Booking.BookingStatus> statuses
    );
    
    Long countByBookingStatus(Booking.BookingStatus status);
}
