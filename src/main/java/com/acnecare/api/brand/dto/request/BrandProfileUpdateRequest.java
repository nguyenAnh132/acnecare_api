package com.acnecare.api.brand.dto.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandProfileUpdateRequest {
    @Size(min = 1, max = 100, message = "INVALID_BRAND_NAME")
    @NotBlank (message = "INVALID_BRAND_NAME")
    String brandName;

    @Size(max = 200, message = "INVALID_DESCRIPTION")
    String description;

    @URL(message = "INVALID_WEBSITE")
    String website;

    @URL(message = "INVALID_LOGO_URL")
    String logoUrl;
}
