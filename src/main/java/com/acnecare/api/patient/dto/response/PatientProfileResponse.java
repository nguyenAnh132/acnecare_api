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
public class PatientProfileResponse {
    boolean gender;
    String skinType;
    String allergies;
    double height;
    double weight;
    String address;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
