package com.acnecare.api.category.service;

import com.acnecare.api.category.dto.request.CategoryCreationRequest;
import com.acnecare.api.category.dto.request.CategoryUpdateRequest;
import com.acnecare.api.category.dto.response.CategoryResponse;
import com.acnecare.api.category.entity.Category;
import com.acnecare.api.category.mapper.CategoryMapper;
import com.acnecare.api.category.repository.CategoryRepository;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CategoryResponse createCategory(CategoryCreationRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        Category category = categoryMapper.toCategory(request);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CategoryResponse updateCategory(String id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        categoryMapper.updateCategory(request, category);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
    }

    // Không có @PreAuthorize vì Public cho phép xem
    public List<CategoryResponse> getAllCategories() {
        return categoryMapper.toCategoryResponseList(categoryRepository.findAll());
    }

    // Không có @PreAuthorize vì Public cho phép xem
    public CategoryResponse getCategoryById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return categoryMapper.toCategoryResponse(category);
    }
}