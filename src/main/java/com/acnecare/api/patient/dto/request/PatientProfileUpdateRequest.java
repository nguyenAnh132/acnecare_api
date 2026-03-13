package com.acnecare.api.patient.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientProfileUpdateRequest {
    boolean gender; //true: male, false: female
    String skinType;
    String allergies;
    double height;


    double weight;

    @Size(max = 255, message = "INVALID_ADDRESS")
    String address;
}
