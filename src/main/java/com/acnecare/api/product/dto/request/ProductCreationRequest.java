package com.acnecare.api.product.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreationRequest {

    String name;
    String brand;
    String description;
    String thumbnailUrl;
    String imagesUrl;
    String ingredients;
    String affiliateUrl;

    // Khóa ngoại: Nhận ID của Category từ Client truyền lên
    String categoryId;

}