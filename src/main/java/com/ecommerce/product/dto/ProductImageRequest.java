package com.ecommerce.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductImageRequest {

    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "Image URL must be less than 500 characters")
    private String imageUrl;

    @Size(max = 255, message = "Alt text must be less than 255 characters")
    private String altText;

    private Integer sortOrder = 0;

    private Boolean isPrimary = false;
}