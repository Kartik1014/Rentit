package com.rentit.dto;

import com.rentit.entity.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDTO {
    private Long id;
    private UserDTO owner;
    private String title;
    private String description;
    private Property.PropertyType propertyType;
    private Double rentAmount;
    private Double deposit;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private Double latitude;
    private Double longitude;
    private Integer bedrooms;
    private Integer bathrooms;
    private Double areaSqft;
    private List<String> amenities;
    private List<ImageDTO> images;
    private Property.AvailabilityStatus availabilityStatus;
    private Boolean isVerified;
    private Long views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
