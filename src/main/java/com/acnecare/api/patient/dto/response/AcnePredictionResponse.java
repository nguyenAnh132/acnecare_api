package com.acnecare.api.patient.dto.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AcnePredictionResponse {
    String id;
    String name;
    String note;
    String originalImageUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
