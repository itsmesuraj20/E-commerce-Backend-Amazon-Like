package com.ecommerce.product.repository;

import com.ecommerce.product.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID> {

    Optional<Brand> findByName(String name);

    List<Brand> findByIsActiveTrueOrderByName();

    @Query("SELECT b FROM Brand b WHERE b.name ILIKE %:name% AND b.isActive = true")
    List<Brand> findByNameContainingIgnoreCase(@Param("name") String name);

    Boolean existsByName(String name);

    Boolean existsByNameAndIdNot(String name, UUID id);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.brand.id = :brandId AND p.isActive = true")
    Long countProductsByBrandId(@Param("brandId") UUID brandId);
}