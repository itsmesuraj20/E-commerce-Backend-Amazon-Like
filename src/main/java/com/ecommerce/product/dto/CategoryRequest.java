package com.ecommerce.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must be less than 100 characters")
    private String name;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @NotBlank(message = "Slug is required")
    @Size(max = 120, message = "Slug must be less than 120 characters")
    private String slug;

    @Size(max = 500, message = "Image URL must be less than 500 characters")
    private String imageUrl;

    private Boolean isActive = true;

    private Integer sortOrder = 0;

    private UUID parentId;
}