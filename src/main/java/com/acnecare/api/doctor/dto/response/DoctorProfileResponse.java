package com.acnecare.api.doctor.dto.response;

import java.time.LocalDate;
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
public class DoctorProfileResponse {
    String userId;
    LocalDate dob;
    String licenseUrl;
    String specialty;
    String bio;
    String clinicName;
    Integer yearsExperience;
    String verificationStatus;
    String rejectionReason;
    Double ratingAvg;
    Integer ratingCount;
    String address;
    boolean isAcceptingAppointments;
}
