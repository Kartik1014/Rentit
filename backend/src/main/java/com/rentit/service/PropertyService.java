package com.rentit.service;

import com.rentit.dto.*;
import com.rentit.entity.Property;
import com.rentit.entity.PropertyImage;
import com.rentit.entity.User;
import com.rentit.exception.ResourceNotFoundException;
import com.rentit.repository.PropertyRepository;
import com.rentit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public PropertyDTO createProperty(PropertyRequest request, String userEmail) {
        User owner = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Property property = modelMapper.map(request, Property.class);
        property.setOwner(owner);
        property.setAvailabilityStatus(Property.AvailabilityStatus.DRAFT);
        property.setIsVerified(false);
        property.setViews(0L);

        if (request.getImages() != null) {
            property.setImages(request.getImages().stream()
                    .map(imageDTO -> PropertyImage.builder()
                            .url(imageDTO.getUrl())
                            .isPrimary(imageDTO.getIsPrimary())
                            .property(property)
                            .build())
                    .collect(Collectors.toList()));
        }

        Property savedProperty = propertyRepository.save(property);
        return mapToDTO(savedProperty);
    }

    public Page<PropertyDTO> getAllProperties(Pageable pageable) {
        Page<Property> properties = propertyRepository.findByDeletedAtIsNull(pageable);
        return properties.map(this::mapToDTO);
    }

    @Transactional
    public PropertyDTO getPropertyById(Long id) {
        Property property = propertyRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        // Increment views
        property.setViews(property.getViews() + 1);
        propertyRepository.save(property);

        return mapToDTO(property);
    }

    @Transactional
    public PropertyDTO updateProperty(Long id, PropertyRequest request, String userEmail) {
        Property property = propertyRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!property.getOwner().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to update this property");
        }

        modelMapper.map(request, property);

        if (request.getImages() != null) {
            property.getImages().clear();
            property.getImages().addAll(request.getImages().stream()
                    .map(imageDTO -> PropertyImage.builder()
                            .url(imageDTO.getUrl())
                            .isPrimary(imageDTO.getIsPrimary())
                            .property(property)
                            .build())
                    .collect(Collectors.toList()));
        }

        Property updatedProperty = propertyRepository.save(property);
        return mapToDTO(updatedProperty);
    }

    @Transactional
    public void deleteProperty(Long id, String userEmail) {
        Property property = propertyRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!property.getOwner().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to delete this property");
        }

        property.setDeletedAt(LocalDateTime.now());
        propertyRepository.save(property);
    }

    public Page<PropertyDTO> getPropertiesByOwner(Long ownerId, Pageable pageable, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getId().equals(ownerId) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to view these properties");
        }

        Page<Property> properties = propertyRepository.findByOwnerIdAndDeletedAtIsNull(ownerId, pageable);
        return properties.map(this::mapToDTO);
    }

    @Transactional
    public PropertyDTO updatePropertyStatus(Long id, Property.AvailabilityStatus status, String userEmail) {
        Property property = propertyRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!property.getOwner().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to update this property");
        }

        property.setAvailabilityStatus(status);
        Property updatedProperty = propertyRepository.save(property);
        return mapToDTO(updatedProperty);
    }

    public Page<PropertyDTO> searchProperties(
            String city,
            Double minPrice,
            Double maxPrice,
            Property.PropertyType propertyType,
            Integer bedrooms,
            Pageable pageable
    ) {
        Page<Property> properties = propertyRepository.searchProperties(
                city, minPrice, maxPrice, propertyType, bedrooms, pageable
        );
        return properties.map(this::mapToDTO);
    }

    private PropertyDTO mapToDTO(Property property) {
        PropertyDTO dto = modelMapper.map(property, PropertyDTO.class);
        dto.setOwner(modelMapper.map(property.getOwner(), UserDTO.class));
        
        if (property.getImages() != null) {
            dto.setImages(property.getImages().stream()
                    .map(img -> ImageDTO.builder()
                            .id(img.getId())
                            .url(img.getUrl())
                            .isPrimary(img.getIsPrimary())
                            .build())
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
}
