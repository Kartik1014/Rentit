package com.rentit.dto;

import com.rentit.entity.Property;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PropertyRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Property type is required")
    private Property.PropertyType propertyType;
    
    @NotNull(message = "Rent amount is required")
    @Min(value = 0, message = "Rent amount must be positive")
    private Double rentAmount;
    
    @NotNull(message = "Deposit is required")
    @Min(value = 0, message = "Deposit must be positive")
    private Double deposit;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "State is required")
    private String state;
    
    @NotBlank(message = "Pincode is required")
    private String pincode;
    
    private Double latitude;
    private Double longitude;
    
    @NotNull(message = "Bedrooms is required")
    @Min(value = 1, message = "Must have at least 1 bedroom")
    private Integer bedrooms;
    
    @NotNull(message = "Bathrooms is required")
    @Min(value = 1, message = "Must have at least 1 bathroom")
    private Integer bathrooms;
    
    private Double areaSqft;
    private List<String> amenities;
    private List<ImageDTO> images;
}
