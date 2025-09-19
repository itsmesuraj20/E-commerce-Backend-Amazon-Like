package com.ecommerce.product.service;

import com.ecommerce.product.dto.CategoryRequest;
import com.ecommerce.product.dto.CategoryResponse;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getRootCategories() {
        List<Category> rootCategories = categoryRepository.findRootCategoriesOrderBySortOrder();
        return rootCategories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getSubCategories(UUID parentId) {
        List<Category> subCategories = categoryRepository.findByParentIdOrderBySortOrder(parentId);
        return subCategories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return convertToCategoryResponse(category);
    }

    public CategoryResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return convertToCategoryResponse(category);
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsBySlug(categoryRequest.getSlug())) {
            throw new RuntimeException("Category with this slug already exists");
        }

        Category.CategoryBuilder categoryBuilder = Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .slug(categoryRequest.getSlug())
                .imageUrl(categoryRequest.getImageUrl())
                .isActive(categoryRequest.getIsActive())
                .sortOrder(categoryRequest.getSortOrder());

        if (categoryRequest.getParentId() != null) {
            Category parent = categoryRepository.findById(categoryRequest.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            categoryBuilder.parent(parent);
        }

        Category category = categoryBuilder.build();
        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryResponse(savedCategory);
    }

    @Transactional
    public CategoryResponse updateCategory(UUID id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (categoryRepository.existsBySlugAndIdNot(categoryRequest.getSlug(), id)) {
            throw new RuntimeException("Category with this slug already exists");
        }

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setSlug(categoryRequest.getSlug());
        category.setImageUrl(categoryRequest.getImageUrl());
        category.setIsActive(categoryRequest.getIsActive());
        category.setSortOrder(categoryRequest.getSortOrder());

        if (categoryRequest.getParentId() != null) {
            Category parent = categoryRepository.findById(categoryRequest.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        Category updatedCategory = categoryRepository.save(category);
        return convertToCategoryResponse(updatedCategory);
    }

    @Transactional
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Check if category has products
        Long productCount = categoryRepository.countProductsByCategoryId(id);
        if (productCount > 0) {
            throw new RuntimeException("Cannot delete category with products. Move products first.");
        }

        // Check if category has children
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            throw new RuntimeException("Cannot delete category with subcategories. Delete subcategories first.");
        }

        categoryRepository.delete(category);
    }

    public List<CategoryResponse> searchCategories(String keyword) {
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(keyword);
        return categories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }

    private CategoryResponse convertToCategoryResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setSlug(category.getSlug());
        response.setImageUrl(category.getImageUrl());
        response.setIsActive(category.getIsActive());
        response.setSortOrder(category.getSortOrder());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());

        if (category.getParent() != null) {
            response.setParentId(category.getParent().getId());
            response.setParentName(category.getParent().getName());
        }

        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            response.setChildren(category.getChildren().stream()
                    .map(this::convertToCategoryResponse)
                    .collect(Collectors.toList()));
        }

        // Get product count
        Long productCount = categoryRepository.countProductsByCategoryId(category.getId());
        response.setProductCount(productCount.intValue());

        return response;
    }
}