package com.acnecare.api.category.controller;

import com.acnecare.api.category.dto.request.CategoryCreationRequest;
import com.acnecare.api.category.dto.request.CategoryUpdateRequest;
import com.acnecare.api.category.dto.response.CategoryResponse;
import com.acnecare.api.category.service.CategoryService;
import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.product.dto.response.ProductResponse;
import com.acnecare.api.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;
    ProductService productService;

    @PostMapping
    ApiResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreationRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .code(1000)
                .message("Category created successfully")
                .result(categoryService.createCategory(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<CategoryResponse> updateCategory(@PathVariable String id,
            @Valid @RequestBody CategoryUpdateRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .code(1000)
                .message("Category updated successfully")
                .result(categoryService.updateCategory(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<CategoryResponse>> getAllCategories() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .code(1000)
                .message("Categories retrieved successfully")
                .result(categoryService.getAllCategories())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<CategoryResponse> getCategoryById(@PathVariable String id) {
        return ApiResponse.<CategoryResponse>builder()
                .code(1000)
                .message("Category retrieved successfully")
                .result(categoryService.getCategoryById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Category deleted successfully")
                .build();
    }

    @GetMapping("/{id}/products")
    ApiResponse<List<ProductResponse>> getProductsByCategory(@PathVariable String id) {
        return ApiResponse.<List<ProductResponse>>builder()
                .code(1000)
                .message("Products retrieved successfully")
                .result(productService.getProductsByCategoryId(id))
                .build();
    }
}