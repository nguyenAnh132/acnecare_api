package com.acnecare.api.category.mapper;

import com.acnecare.api.category.dto.request.CategoryCreationRequest;
import com.acnecare.api.category.dto.request.CategoryUpdateRequest;
import com.acnecare.api.category.dto.response.CategoryResponse;
import com.acnecare.api.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryCreationRequest request);

    CategoryResponse toCategoryResponse(Category category);

    void updateCategory(CategoryUpdateRequest request, @MappingTarget Category category);

    List<CategoryResponse> toCategoryResponseList(List<Category> categories);
}