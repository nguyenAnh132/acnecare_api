package com.acnecare.api.product.dto.response;

import com.acnecare.api.category.dto.response.CategoryResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    String brand;
    String description;
    String thumbnailUrl;
    String imagesUrl;
    String ingredients;
    String affiliateUrl;
    String approvalStatus;
    LocalDateTime createdAt;

    CategoryResponse category;
    String createdBy; // ID của người tạo
}