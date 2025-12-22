package com.rentit.service;

import com.rentit.dto.MessageResponse;
import com.rentit.dto.ReviewDTO;
import com.rentit.dto.ReviewRequest;
import com.rentit.dto.UserDTO;
import com.rentit.entity.Booking;
import com.rentit.entity.Property;
import com.rentit.entity.Review;
import com.rentit.entity.User;
import com.rentit.exception.ResourceNotFoundException;
import com.rentit.repository.BookingRepository;
import com.rentit.repository.PropertyRepository;
import com.rentit.repository.ReviewRepository;
import com.rentit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ReviewDTO submitReview(ReviewRequest request, String userEmail) {
        User tenant = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Property property = propertyRepository.findByIdAndDeletedAtIsNull(request.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        // Check if user has an approved booking
        List<Booking> approvedBookings = bookingRepository.findByPropertyIdAndTenantIdAndBookingStatusIn(
                property.getId(),
                tenant.getId(),
                List.of(Booking.BookingStatus.APPROVED)
        ).stream().toList();

        if (approvedBookings.isEmpty()) {
            throw new RuntimeException("You can only review properties you have booked");
        }

        // Check if review already exists
        reviewRepository.findByPropertyIdAndTenantId(property.getId(), tenant.getId())
                .ifPresent(r -> {
                    throw new RuntimeException("You have already reviewed this property");
                });

        Review review = Review.builder()
                .property(property)
                .tenant(tenant)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);
        return mapToDTO(savedReview);
    }

    public Map<String, Object> getPropertyReviews(Long propertyId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByPropertyId(propertyId, pageable);
        
        Double avgRating = reviewRepository.getAverageRatingByPropertyId(propertyId);
        Long totalReviews = reviewRepository.countByPropertyId(propertyId);

        List<ReviewDTO> reviewDTOs = reviews.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("reviews", reviewDTOs);
        response.put("currentPage", reviews.getNumber());
        response.put("totalPages", reviews.getTotalPages());
        response.put("totalReviews", totalReviews);
        response.put("averageRating", avgRating != null ? String.format("%.1f", avgRating) : "0.0");

        return response;
    }

    @Transactional
    public ReviewDTO updateReview(Long id, ReviewRequest request, String userEmail) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!review.getTenant().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to update this review");
        }

        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }
        if (request.getComment() != null) {
            review.setComment(request.getComment());
        }

        Review updatedReview = reviewRepository.save(review);
        return mapToDTO(updatedReview);
    }

    @Transactional
    public MessageResponse deleteReview(Long id, String userEmail) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!review.getTenant().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to delete this review");
        }

        reviewRepository.delete(review);
        return new MessageResponse("Review deleted successfully");
    }

    private ReviewDTO mapToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setPropertyId(review.getProperty().getId());
        dto.setTenant(modelMapper.map(review.getTenant(), UserDTO.class));
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        return dto;
    }
}
