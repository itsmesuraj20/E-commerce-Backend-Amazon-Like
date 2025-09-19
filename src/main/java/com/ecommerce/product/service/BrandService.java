package com.ecommerce.product.service;

import com.ecommerce.product.dto.BrandResponse;
import com.ecommerce.product.entity.Brand;
import com.ecommerce.product.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public List<BrandResponse> getAllActiveBrands() {
        return brandRepository.findByIsActiveTrueOrderByName()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BrandResponse getBrandById(UUID id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + id));
        if (!brand.getIsActive()) {
            throw new RuntimeException("Brand is not active");
        }
        return mapToResponse(brand);
    }

    public List<BrandResponse> searchBrands(String keyword) {
        return brandRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private BrandResponse mapToResponse(Brand brand) {
        BrandResponse response = new BrandResponse();
        response.setId(brand.getId());
        response.setName(brand.getName());
        response.setDescription(brand.getDescription());
        response.setLogoUrl(brand.getLogoUrl());
        response.setCreatedAt(brand.getCreatedAt());
        response.setUpdatedAt(brand.getUpdatedAt());
        return response;
    }
}