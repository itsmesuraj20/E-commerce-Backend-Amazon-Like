package com.ecommerce.product.repository;

import com.ecommerce.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findBySlug(String slug);

    List<Category> findByParentIsNullAndIsActiveTrue();

    List<Category> findByParentIdAndIsActiveTrue(UUID parentId);

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.isActive = true ORDER BY c.sortOrder ASC")
    List<Category> findRootCategoriesOrderBySortOrder();

    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId AND c.isActive = true ORDER BY c.sortOrder ASC")
    List<Category> findByParentIdOrderBySortOrder(@Param("parentId") UUID parentId);

    @Query("SELECT c FROM Category c WHERE c.name ILIKE %:name% AND c.isActive = true")
    List<Category> findByNameContainingIgnoreCase(@Param("name") String name);

    Boolean existsBySlug(String slug);

    Boolean existsBySlugAndIdNot(String slug, UUID id);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId AND p.isActive = true")
    Long countProductsByCategoryId(@Param("categoryId") UUID categoryId);
}