package com.acnecare.api.consultation_service.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class ConsultationServiceUpdateRequest {

    @NotBlank(message = "INVALID_SERVICE_NAME")
    @Size(max = 150, message = "INVALID_SERVICE_NAME")
    String serviceName;

    @NotBlank(message = "INVALID_MODE")
    @Size(max = 20, message = "INVALID_MODE")
    String mode;

    @NotNull(message = "INVALID_DURATION")
    @Positive(message = "INVALID_DURATION")
    Integer durationMinutes;

    @NotNull(message = "INVALID_PRICE")
    @DecimalMin(value = "0.0", inclusive = true, message = "INVALID_PRICE")
    BigDecimal price;

    @NotBlank(message = "INVALID_CURRENCY")
    @Size(max = 10, message = "INVALID_CURRENCY")
    String currency;

    @Size(max = 1000, message = "INVALID_DESCRIPTION")
    String description;

    @NotNull(message = "INVALID_ACTIVE_STATUS")
    Boolean isActive;
}