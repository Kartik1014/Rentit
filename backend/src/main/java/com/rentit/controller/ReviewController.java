package com.rentit.controller;

import com.rentit.dto.MessageResponse;
import com.rentit.dto.ReviewDTO;
import com.rentit.dto.ReviewRequest;
import com.rentit.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<Map<String, Object>> submitReview(
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication
    ) {
        ReviewDTO review = reviewService.submitReview(request, authentication.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Review submitted successfully");
        response.put("review", review);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<Map<String, Object>> getPropertyReviews(
            @PathVariable Long propertyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Map<String, Object> response = reviewService.getPropertyReviews(propertyId, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> updateReview(
            @PathVariable Long id,
            @RequestBody ReviewRequest request,
            Authentication authentication
    ) {
        ReviewDTO review = reviewService.updateReview(id, request, authentication.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Review updated successfully");
        response.put("review", review);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT', 'ADMIN')")
    public ResponseEntity<MessageResponse> deleteReview(
            @PathVariable Long id,
            Authentication authentication
    ) {
        MessageResponse response = reviewService.deleteReview(id, authentication.getName());
        return ResponseEntity.ok(response);
    }
}
