package com.acnecare.api.brand.dto.response;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandProfileResponse{
    String brandName;
    String description;
    String website;
    String logoUrl;
    String verificationStatus;
    String rejectionReason;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
