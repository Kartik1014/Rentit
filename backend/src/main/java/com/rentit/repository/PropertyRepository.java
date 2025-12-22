package com.rentit.repository;

import com.rentit.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {
    
    Page<Property> findByDeletedAtIsNull(Pageable pageable);
    
    Page<Property> findByOwnerIdAndDeletedAtIsNull(Long ownerId, Pageable pageable);
    
    Page<Property> findByIsVerifiedFalseAndDeletedAtIsNull(Pageable pageable);
    
    Optional<Property> findByIdAndDeletedAtIsNull(Long id);
    
    @Query("SELECT p FROM Property p WHERE p.deletedAt IS NULL " +
           "AND (:city IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
           "AND (:minPrice IS NULL OR p.rentAmount >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.rentAmount <= :maxPrice) " +
           "AND (:propertyType IS NULL OR p.propertyType = :propertyType) " +
           "AND (:bedrooms IS NULL OR p.bedrooms >= :bedrooms)")
    Page<Property> searchProperties(
        @Param("city") String city,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("propertyType") Property.PropertyType propertyType,
        @Param("bedrooms") Integer bedrooms,
        Pageable pageable
    );
}
