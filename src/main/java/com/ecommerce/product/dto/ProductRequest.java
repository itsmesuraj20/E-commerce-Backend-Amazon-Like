package com.ecommerce.product.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must be less than 255 characters")
    private String name;

    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;

    @Size(max = 500, message = "Short description must be less than 500 characters")
    private String shortDescription;

    @NotBlank(message = "SKU is required")
    @Size(max = 100, message = "SKU must be less than 100 characters")
    private String sku;

    @NotBlank(message = "Slug is required")
    @Size(max = 255, message = "Slug must be less than 255 characters")
    private String slug;

    @NotNull(message = "Category is required")
    private UUID categoryId;

    private UUID brandId;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price format is invalid")
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "Compare price must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Compare price format is invalid")
    private BigDecimal comparePrice;

    @DecimalMin(value = "0.0", message = "Cost price must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Cost price format is invalid")
    private BigDecimal costPrice;

    @DecimalMin(value = "0.0", message = "Weight must be greater than or equal to 0")
    @Digits(integer = 6, fraction = 2, message = "Weight format is invalid")
    private BigDecimal weight;

    @Size(max = 50, message = "Dimensions must be less than 50 characters")
    private String dimensions;

    private Boolean isActive = true;

    private Boolean isFeatured = false;

    private Boolean requiresShipping = true;

    private Boolean taxable = true;

    private Boolean trackInventory = true;

    @Size(max = 255, message = "SEO title must be less than 255 characters")
    private String seoTitle;

    @Size(max = 1000, message = "SEO description must be less than 1000 characters")
    private String seoDescription;

    private List<ProductImageRequest> images;

    private List<ProductAttributeRequest> attributes;
}