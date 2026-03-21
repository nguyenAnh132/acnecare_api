package com.acnecare.api.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
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

    @NotBlank(message = "INVALID_PRODUCT_NAME")
    @Size(min = 2, max = 255, message = "INVALID_PRODUCT_NAME")
    String name;

    @NotBlank(message = "INVALID_PRODUCT_BRAND")
    String brand;

    @NotBlank(message = "INVALID_PRODUCT_DESCRIPTION")
    String description;

    @URL(message = "INVALID_PRODUCT_URL")
    String thumbnailUrl;

    String imagesUrl;

    String ingredients;

    @URL(message = "INVALID_PRODUCT_URL")
    String affiliateUrl;

    @NotBlank(message = "INVALID_CATEGORY_ID")
    String categoryId;

}