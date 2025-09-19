package com.ecommerce.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductAttributeRequest {

    @NotBlank(message = "Attribute name is required")
    @Size(max = 100, message = "Attribute name must be less than 100 characters")
    private String attributeName;

    @NotBlank(message = "Attribute value is required")
    @Size(max = 1000, message = "Attribute value must be less than 1000 characters")
    private String attributeValue;
}