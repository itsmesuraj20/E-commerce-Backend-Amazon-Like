package com.ecommerce.product.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class ProductSearchCriteria {
    
    private String keyword; // Search in name, description
    private UUID categoryId;
    private UUID brandId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal minRating;
    private Boolean isActive = true;
    private Boolean isFeatured;
    private Boolean inStock;
    private List<String> tags;
    
    // Sorting
    private String sortBy = "createdAt"; // name, price, rating, createdAt
    private String sortDirection = "desc"; // asc, desc
    
    // Pagination
    private Integer page = 0;
    private Integer size = 20;
}