package com.ecommerce.product.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductAttributeResponse {
    private UUID id;
    private String attributeName;
    private String attributeValue;
    private LocalDateTime createdAt;
}