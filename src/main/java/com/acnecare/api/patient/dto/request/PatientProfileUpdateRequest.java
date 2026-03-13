package com.acnecare.api.patient.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientProfileUpdateRequest {

    @NotNull(message = "INVALID_GENDER")
    boolean gender;
    
    @Size(max = 255, message = "INVALID_SKIN_TYPE")
    String skinType;

    @Size(max = 255, message = "INVALID_ALLERGIES")
    String allergies;

    @NotNull(message = "INVALID_HEIGHT")
    @Positive(message = "INVALID_HEIGHT")
    double height;

    @NotNull(message = "INVALID_WEIGHT")
    @Positive(message = "INVALID_WEIGHT")
    double weight;

    @Size(max = 255, message = "INVALID_ADDRESS")
    String address;
}
