package com.acnecare.api.brand.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @Size(max = 200, message = "INVALID_WEBSITE")
    String website;

    @Size(max = 200, message = "INVALID_LOGO_URL")
    String logoUrl;

    @Email(message = "INVALID_EMAIL")
    String email;

    @Pattern(regexp = "^0[0-9]{9}$", message = "INVALID_PHONE")
    String phone;
}
