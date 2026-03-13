package com.acnecare.api.doctor.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.PositiveOrZero;
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
public class DoctorProfileUpdateRequest {

    LocalDate dob;

    @Size(max = 50, message = "INVALID_LICENSE_NO")
    String licenseNo;

    @Size(max = 100, message = "INVALID_SPECIALTY")
    String specialty;

    @Size(max = 500, message = "INVALID_BIO")
    String bio;

    @Size(max = 200, message = "INVALID_CLINIC_NAME")
    String clinicName;

    @PositiveOrZero(message = "INVALID_YEARS_EXPERIENCE")
    Integer yearsExperience;

    @Size(max = 255, message = "INVALID_ADDRESS")
    String address;

    Boolean isAcceptingAppointments;
}