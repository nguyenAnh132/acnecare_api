package com.acnecare.api.product.mapper;

import com.acnecare.api.category.mapper.CategoryMapper;
import com.acnecare.api.product.dto.request.ProductCreationRequest;
import com.acnecare.api.product.dto.request.ProductUpdateRequest;
import com.acnecare.api.product.dto.response.ProductResponse;
import com.acnecare.api.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring", uses = { CategoryMapper.class })
public interface ProductMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "approvalStatus", ignore = true)
    Product toProduct(ProductCreationRequest request);

    // Lấy ID từ object User (createdBy) để gán vào chuỗi createdBy của DTO
    @Mapping(source = "createdBy.id", target = "createdBy")
    ProductResponse toProductResponse(Product product);

    List<ProductResponse> toProductResponseList(List<Product> products);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "approvalStatus", ignore = true)
    void updateProduct(ProductUpdateRequest request, @MappingTarget Product product);
}