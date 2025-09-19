package com.ecommerce.product.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BrandResponse {
    private UUID id;
    private String name;
    private String description;
    private String logoUrl;
    private String websiteUrl;
    private Boolean isActive;
    private Integer productCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}