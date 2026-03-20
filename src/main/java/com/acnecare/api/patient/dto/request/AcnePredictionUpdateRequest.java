package com.acnecare.api.patient.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AcnePredictionUpdateRequest {

    @NotEmpty(message = "INVALID_NAME")
    @Size(max = 255, message = "INVALID_NAME")
    String name;

    @NotEmpty(message = "INVALID_NOTE")
    @Size(max = 255, message = "INVALID_NOTE")
    String note;

    @NotBlank(message = "INVALID_ORIGINAL_IMAGE_URL")
    @URL(message = "INVALID_ORIGINAL_IMAGE_URL")
    String originalImageUrl;

}
