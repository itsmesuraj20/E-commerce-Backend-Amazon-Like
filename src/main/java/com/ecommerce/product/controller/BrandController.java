package com.ecommerce.product.controller;

import com.ecommerce.product.dto.BrandResponse;
import com.ecommerce.product.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/brands")
@Tag(name = "Brand Management", description = "Brand operations")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    @Operation(summary = "Get all active brands")
    public ResponseEntity<List<BrandResponse>> getAllBrands() {
        List<BrandResponse> brands = brandService.getAllActiveBrands();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get brand by ID")
    public ResponseEntity<BrandResponse> getBrandById(@PathVariable UUID id) {
        BrandResponse brand = brandService.getBrandById(id);
        return ResponseEntity.ok(brand);
    }

    @GetMapping("/search")
    @Operation(summary = "Search brands by name")
    public ResponseEntity<List<BrandResponse>> searchBrands(@RequestParam String keyword) {
        List<BrandResponse> brands = brandService.searchBrands(keyword);
        return ResponseEntity.ok(brands);
    }
}