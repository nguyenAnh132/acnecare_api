package com.acnecare.api.doctor.dto.request;

import java.time.LocalDate;

import org.hibernate.validator.constraints.URL;

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
public class DoctorProfileUpdateRequest {

    LocalDate dob;

    @NotBlank(message = "INVALID_LICENSE_URL")
    @URL(message = "INVALID_LICENSE_URL")
    String licenseUrl;

    @Size(max = 100, message = "INVALID_SPECIALTY")
    String specialty;

    @Size(max = 500, message = "INVALID_BIO")
    String bio;

    @NotBlank(message = "INVALID_CLINIC_NAME")
    @Size(max = 200, message = "INVALID_CLINIC_NAME")
    String clinicName;

    @NotNull(message = "INVALID_YEARS_EXPERIENCE")
    @PositiveOrZero(message = "INVALID_YEARS_EXPERIENCE")
    Integer yearsExperience;

    @NotBlank(message = "INVALID_ADDRESS")
    @Size(max = 255, message = "INVALID_ADDRESS")
    String address;

    @NotNull(message = "INVALID_ACCEPTING_APPOINTMENTS")
    Boolean isAcceptingAppointments = false;
}