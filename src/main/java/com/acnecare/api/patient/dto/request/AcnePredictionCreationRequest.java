package com.acnecare.api.patient.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AcnePredictionCreationRequest {
    
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
