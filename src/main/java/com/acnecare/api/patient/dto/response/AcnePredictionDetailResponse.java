package com.acnecare.api.patient.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AcnePredictionDetailResponse {

    String acnePredictionId;

    String acneId;

    Double averageConfidence;

    Integer quantity;

    LocalDateTime createdAt;

}
