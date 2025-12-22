package com.rentit.controller;

import com.rentit.dto.MessageResponse;
import com.rentit.dto.PropertyDTO;
import com.rentit.dto.PropertyRequest;
import com.rentit.entity.Property;
import com.rentit.service.PropertyService;
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
@RequestMapping("/api/properties")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> createProperty(
            @Valid @RequestBody PropertyRequest request,
            Authentication authentication
    ) {
        PropertyDTO property = propertyService.createProperty(request, authentication.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Property created successfully");
        response.put("property", property);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order
    ) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sortBy));
        
        Page<PropertyDTO> properties = propertyService.getAllProperties(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("properties", properties.getContent());
        response.put("currentPage", properties.getNumber());
        response.put("totalPages", properties.getTotalPages());
        response.put("totalProperties", properties.getTotalElements());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPropertyById(@PathVariable Long id) {
        PropertyDTO property = propertyService.getPropertyById(id);
        return ResponseEntity.ok(Map.of("property", property));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> updateProperty(
            @PathVariable Long id,
            @Valid @RequestBody PropertyRequest request,
            Authentication authentication
    ) {
        PropertyDTO property = propertyService.updateProperty(id, request, authentication.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Property updated successfully");
        response.put("property", property);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<MessageResponse> deleteProperty(
            @PathVariable Long id,
            Authentication authentication
    ) {
        propertyService.deleteProperty(id, authentication.getName());
        return ResponseEntity.ok(new MessageResponse("Property deleted successfully"));
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getPropertiesByOwner(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PropertyDTO> properties = propertyService.getPropertiesByOwner(ownerId, pageable, authentication.getName());
        
        Map<String, Object> response = new HashMap<>();
        response.put("properties", properties.getContent());
        response.put("currentPage", properties.getNumber());
        response.put("totalPages", properties.getTotalPages());
        response.put("totalProperties", properties.getTotalElements());
        
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> updatePropertyStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            Authentication authentication
    ) {
        Property.AvailabilityStatus status = Property.AvailabilityStatus.valueOf(request.get("status"));
        PropertyDTO property = propertyService.updatePropertyStatus(id, status, authentication.getName());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Property status updated successfully");
        response.put("property", property);
        
        return ResponseEntity.ok(response);
    }
}
