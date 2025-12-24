package com.rentit.controller;

import com.rentit.dto.PropertyDTO;
import com.rentit.entity.Property;
import com.rentit.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class SearchController {

    private final PropertyService propertyService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> searchProperties(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Property.PropertyType propertyType,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false) Integer bathrooms,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sortBy));
        
        Page<PropertyDTO> properties = propertyService.searchProperties(
                location, minPrice, maxPrice, propertyType, bedrooms, pageable
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("properties", properties.getContent());
        response.put("currentPage", properties.getNumber());
        response.put("totalPages", properties.getTotalPages());
        response.put("totalProperties", properties.getTotalElements());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nearby")
    public ResponseEntity<Map<String, Object>> searchNearby(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "10") Double radius,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        // For simplicity, using the same search method
        // In production, implement actual geospatial queries
        Pageable pageable = PageRequest.of(page, limit);
        Page<PropertyDTO> properties = propertyService.getAllProperties(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("properties", properties.getContent());
        response.put("currentPage", properties.getNumber());
        response.put("totalPages", properties.getTotalPages());
        response.put("totalProperties", properties.getTotalElements());
        
        return ResponseEntity.ok(response);
    }
}
