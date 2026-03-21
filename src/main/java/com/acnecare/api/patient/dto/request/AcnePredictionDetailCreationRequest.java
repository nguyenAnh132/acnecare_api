package com.acnecare.api.patient.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AcnePredictionDetailCreationRequest {

    @NotBlank(message = "INVALID_ACNE_PREDICTION_ID")
    String acnePredictionId;

    @NotBlank(message = "INVALID_ACNE_ID")
    String acneId;

    @NotNull(message = "INVALID_AVERAGE_CONFIDENCE")
    @DecimalMin(value = "0.0", inclusive = true, message = "INVALID_AVERAGE_CONFIDENCE")
    @DecimalMax(value = "1.0", inclusive = true, message = "INVALID_AVERAGE_CONFIDENCE")
    Double averageConfidence;

    @Positive(message = "INVALID_QUANTITY")
    @NotNull(message = "INVALID_QUANTITY")
    Integer quantity;

}
