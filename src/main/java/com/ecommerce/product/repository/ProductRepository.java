package com.ecommerce.product.repository;

import com.ecommerce.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Optional<Product> findBySlug(String slug);

    Optional<Product> findBySku(String sku);

    Page<Product> findByIsActiveTrue(Pageable pageable);

    Page<Product> findByIsFeaturedTrueAndIsActiveTrue(Pageable pageable);

    Page<Product> findByCategoryIdAndIsActiveTrue(UUID categoryId, Pageable pageable);

    Page<Product> findByBrandIdAndIsActiveTrue(UUID brandId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.price BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                  @Param("maxPrice") BigDecimal maxPrice, 
                                  Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.rating >= :minRating")
    Page<Product> findByMinRating(@Param("minRating") BigDecimal minRating, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds AND p.isActive = true")
    Page<Product> findByCategoryIds(@Param("categoryIds") List<UUID> categoryIds, Pageable pageable);

    Boolean existsBySlug(String slug);

    Boolean existsBySku(String sku);

    Boolean existsBySlugAndIdNot(String slug, UUID id);

    Boolean existsBySkuAndIdNot(String sku, UUID id);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images LEFT JOIN FETCH p.attributes WHERE p.id = :id")
    Optional<Product> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images LEFT JOIN FETCH p.attributes WHERE p.slug = :slug")
    Optional<Product> findBySlugWithDetails(@Param("slug") String slug);

    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN p.category c " +
           "LEFT JOIN p.brand b " +
           "WHERE p.isActive = true " +
           "AND (:keyword IS NULL OR " +
           "    LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "    LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "    LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:categoryId IS NULL OR c.id = :categoryId) " +
           "AND (:brandId IS NULL OR b.id = :brandId) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:minRating IS NULL OR p.rating >= :minRating) " +
           "AND (:isFeatured IS NULL OR p.isFeatured = :isFeatured)")
    Page<Product> findWithFilters(@Param("keyword") String keyword,
                                 @Param("categoryId") UUID categoryId,
                                 @Param("brandId") UUID brandId,
                                 @Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice,
                                 @Param("minRating") BigDecimal minRating,
                                 @Param("isFeatured") Boolean isFeatured,
                                 Pageable pageable);
}