package com.ecommerce.product.service;

import com.ecommerce.product.dto.*;
import com.ecommerce.product.entity.*;
import com.ecommerce.product.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findByIsActiveTrue(pageable);
        return products.map(this::convertToProductResponse);
    }

    public Page<ProductResponse> getFeaturedProducts(Pageable pageable) {
        Page<Product> products = productRepository.findByIsFeaturedTrueAndIsActiveTrue(pageable);
        return products.map(this::convertToProductResponse);
    }

    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToProductResponse(product);
    }

    public ProductResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlugWithDetails(slug)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToProductResponse(product);
    }

    public Page<ProductResponse> getProductsByCategory(UUID categoryId, Pageable pageable) {
        Page<Product> products = productRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable);
        return products.map(this::convertToProductResponse);
    }

    public Page<ProductResponse> getProductsByBrand(UUID brandId, Pageable pageable) {
        Page<Product> products = productRepository.findByBrandIdAndIsActiveTrue(brandId, pageable);
        return products.map(this::convertToProductResponse);
    }

    public Page<ProductResponse> searchProducts(ProductSearchCriteria criteria) {
        Sort sort = createSort(criteria.getSortBy(), criteria.getSortDirection());
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);

        Page<Product> products = productRepository.findWithFilters(
                criteria.getKeyword(),
                criteria.getCategoryId(),
                criteria.getBrandId(),
                criteria.getMinPrice(),
                criteria.getMaxPrice(),
                criteria.getMinRating(),
                criteria.getIsFeatured(),
                pageable);

        return products.map(this::convertToProductResponse);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        if (productRepository.existsBySku(productRequest.getSku())) {
            throw new RuntimeException("Product with this SKU already exists");
        }

        if (productRepository.existsBySlug(productRequest.getSlug())) {
            throw new RuntimeException("Product with this slug already exists");
        }

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Brand brand = null;
        if (productRequest.getBrandId() != null) {
            brand = brandRepository.findById(productRequest.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Brand not found"));
        }

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .shortDescription(productRequest.getShortDescription())
                .sku(productRequest.getSku())
                .slug(productRequest.getSlug())
                .category(category)
                .brand(brand)
                .price(productRequest.getPrice())
                .comparePrice(productRequest.getComparePrice())
                .costPrice(productRequest.getCostPrice())
                .weight(productRequest.getWeight())
                .dimensions(productRequest.getDimensions())
                .isActive(productRequest.getIsActive())
                .isFeatured(productRequest.getIsFeatured())
                .requiresShipping(productRequest.getRequiresShipping())
                .taxable(productRequest.getTaxable())
                .trackInventory(productRequest.getTrackInventory())
                .seoTitle(productRequest.getSeoTitle())
                .seoDescription(productRequest.getSeoDescription())
                .build();

        Product savedProduct = productRepository.save(product);

        // Save product images
        if (productRequest.getImages() != null && !productRequest.getImages().isEmpty()) {
            List<ProductImage> images = productRequest.getImages().stream()
                    .map(imageRequest -> ProductImage.builder()
                            .product(savedProduct)
                            .imageUrl(imageRequest.getImageUrl())
                            .altText(imageRequest.getAltText())
                            .sortOrder(imageRequest.getSortOrder())
                            .isPrimary(imageRequest.getIsPrimary())
                            .build())
                    .collect(Collectors.toList());
            savedProduct.setImages(images);
        }

        // Save product attributes
        if (productRequest.getAttributes() != null && !productRequest.getAttributes().isEmpty()) {
            List<ProductAttribute> attributes = productRequest.getAttributes().stream()
                    .map(attrRequest -> ProductAttribute.builder()
                            .product(savedProduct)
                            .attributeName(attrRequest.getAttributeName())
                            .attributeValue(attrRequest.getAttributeValue())
                            .build())
                    .collect(Collectors.toList());
            savedProduct.setAttributes(attributes);
        }

        Product finalProduct = productRepository.save(savedProduct);
        return convertToProductResponse(finalProduct);
    }

    @Transactional
    public ProductResponse updateProduct(UUID id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (productRepository.existsBySkuAndIdNot(productRequest.getSku(), id)) {
            throw new RuntimeException("Product with this SKU already exists");
        }

        if (productRepository.existsBySlugAndIdNot(productRequest.getSlug(), id)) {
            throw new RuntimeException("Product with this slug already exists");
        }

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Brand brand = null;
        if (productRequest.getBrandId() != null) {
            brand = brandRepository.findById(productRequest.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Brand not found"));
        }

        // Update product fields
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setShortDescription(productRequest.getShortDescription());
        product.setSku(productRequest.getSku());
        product.setSlug(productRequest.getSlug());
        product.setCategory(category);
        product.setBrand(brand);
        product.setPrice(productRequest.getPrice());
        product.setComparePrice(productRequest.getComparePrice());
        product.setCostPrice(productRequest.getCostPrice());
        product.setWeight(productRequest.getWeight());
        product.setDimensions(productRequest.getDimensions());
        product.setIsActive(productRequest.getIsActive());
        product.setIsFeatured(productRequest.getIsFeatured());
        product.setRequiresShipping(productRequest.getRequiresShipping());
        product.setTaxable(productRequest.getTaxable());
        product.setTrackInventory(productRequest.getTrackInventory());
        product.setSeoTitle(productRequest.getSeoTitle());
        product.setSeoDescription(productRequest.getSeoDescription());

        // Clear and update images
        if (product.getImages() != null) {
            product.getImages().clear();
        }
        if (productRequest.getImages() != null && !productRequest.getImages().isEmpty()) {
            List<ProductImage> images = productRequest.getImages().stream()
                    .map(imageRequest -> ProductImage.builder()
                            .product(product)
                            .imageUrl(imageRequest.getImageUrl())
                            .altText(imageRequest.getAltText())
                            .sortOrder(imageRequest.getSortOrder())
                            .isPrimary(imageRequest.getIsPrimary())
                            .build())
                    .collect(Collectors.toList());
            product.setImages(images);
        }

        // Clear and update attributes
        if (product.getAttributes() != null) {
            product.getAttributes().clear();
        }
        if (productRequest.getAttributes() != null && !productRequest.getAttributes().isEmpty()) {
            List<ProductAttribute> attributes = productRequest.getAttributes().stream()
                    .map(attrRequest -> ProductAttribute.builder()
                            .product(product)
                            .attributeName(attrRequest.getAttributeName())
                            .attributeValue(attrRequest.getAttributeValue())
                            .build())
                    .collect(Collectors.toList());
            product.setAttributes(attributes);
        }

        Product updatedProduct = productRepository.save(product);
        return convertToProductResponse(updatedProduct);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Soft delete by setting isActive to false
        product.setIsActive(false);
        productRepository.save(product);
    }

    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return switch (sortBy.toLowerCase()) {
            case "name" -> Sort.by(direction, "name");
            case "price" -> Sort.by(direction, "price");
            case "rating" -> Sort.by(direction, "rating");
            case "createdat" -> Sort.by(direction, "createdAt");
            default -> Sort.by(direction, "createdAt");
        };
    }

    private ProductResponse convertToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setShortDescription(product.getShortDescription());
        response.setSku(product.getSku());
        response.setSlug(product.getSlug());
        response.setPrice(product.getPrice());
        response.setComparePrice(product.getComparePrice());
        response.setCostPrice(product.getCostPrice());
        response.setWeight(product.getWeight());
        response.setDimensions(product.getDimensions());
        response.setIsActive(product.getIsActive());
        response.setIsFeatured(product.getIsFeatured());
        response.setRequiresShipping(product.getRequiresShipping());
        response.setTaxable(product.getTaxable());
        response.setTrackInventory(product.getTrackInventory());
        response.setSeoTitle(product.getSeoTitle());
        response.setSeoDescription(product.getSeoDescription());
        response.setRating(product.getRating());
        response.setReviewCount(product.getReviewCount());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        // Convert category
        if (product.getCategory() != null) {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(product.getCategory().getId());
            categoryResponse.setName(product.getCategory().getName());
            categoryResponse.setSlug(product.getCategory().getSlug());
            response.setCategory(categoryResponse);
        }

        // Convert brand
        if (product.getBrand() != null) {
            BrandResponse brandResponse = new BrandResponse();
            brandResponse.setId(product.getBrand().getId());
            brandResponse.setName(product.getBrand().getName());
            brandResponse.setLogoUrl(product.getBrand().getLogoUrl());
            response.setBrand(brandResponse);
        }

        // Convert images
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            response.setImages(product.getImages().stream()
                    .map(this::convertToProductImageResponse)
                    .collect(Collectors.toList()));
        }

        // Convert attributes
        if (product.getAttributes() != null && !product.getAttributes().isEmpty()) {
            response.setAttributes(product.getAttributes().stream()
                    .map(this::convertToProductAttributeResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    private ProductImageResponse convertToProductImageResponse(ProductImage image) {
        ProductImageResponse response = new ProductImageResponse();
        response.setId(image.getId());
        response.setImageUrl(image.getImageUrl());
        response.setAltText(image.getAltText());
        response.setSortOrder(image.getSortOrder());
        response.setIsPrimary(image.getIsPrimary());
        response.setCreatedAt(image.getCreatedAt());
        return response;
    }

    private ProductAttributeResponse convertToProductAttributeResponse(ProductAttribute attribute) {
        ProductAttributeResponse response = new ProductAttributeResponse();
        response.setId(attribute.getId());
        response.setAttributeName(attribute.getAttributeName());
        response.setAttributeValue(attribute.getAttributeValue());
        response.setCreatedAt(attribute.getCreatedAt());
        return response;
    }
}