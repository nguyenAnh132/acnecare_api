package com.acnecare.api.patient.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientProfileCreationRequest {

    //validate all fields
    boolean gender; //true: male, false: female
    String skinType;
    String allergies;
    double height;
    double weight;
    String address;

}
