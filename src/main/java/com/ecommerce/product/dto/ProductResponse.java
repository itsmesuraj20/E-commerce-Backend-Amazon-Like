package com.ecommerce.product.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private String shortDescription;
    private String sku;
    private String slug;
    private CategoryResponse category;
    private BrandResponse brand;
    private BigDecimal price;
    private BigDecimal comparePrice;
    private BigDecimal costPrice;
    private BigDecimal weight;
    private String dimensions;
    private Boolean isActive;
    private Boolean isFeatured;
    private Boolean requiresShipping;
    private Boolean taxable;
    private Boolean trackInventory;
    private String seoTitle;
    private String seoDescription;
    private BigDecimal rating;
    private Integer reviewCount;
    private List<ProductImageResponse> images;
    private List<ProductAttributeResponse> attributes;
    private Integer stockQuantity; // From inventory service
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}