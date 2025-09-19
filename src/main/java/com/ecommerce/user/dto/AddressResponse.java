package com.ecommerce.user.dto;

import com.ecommerce.user.entity.Address;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AddressResponse {
    private UUID id;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private Boolean isDefault;
    private Address.AddressType addressType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}