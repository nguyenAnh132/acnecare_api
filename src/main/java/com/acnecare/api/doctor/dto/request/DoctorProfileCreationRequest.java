package com.acnecare.api.doctor.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class DoctorProfileCreationRequest {

    @NotNull(message = "INVALID_DOB")
    LocalDate dob;

    @NotBlank(message = "INVALID_LICENSE_NO")
    String licenseNo;

    @NotBlank(message = "INVALID_SPECIALTY")
    String specialty;

    @Size(max = 500, message = "INVALID_BIO")
    String bio;

    @NotBlank(message = "INVALID_CLINIC_NAME")
    String clinicName;

    @PositiveOrZero(message = "INVALID_YEARS_EXPERIENCE")
    Integer yearsExperience;

    @NotBlank(message = "INVALID_ADDRESS")
    String address;

    Boolean isAcceptingAppointments;
}