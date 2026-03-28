package com.acnecare.api.appointment.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentResponse {

    String id;
    LocalDateTime appointmentTime;
    String status;
    String mode;
    String meetingUrl;
    String paymentMethod;
    String paymentStatus;
    String note;
    String review;
    Integer rating;
    LocalDateTime createdAt;

    String patientId;
    String patientName;
    String patientAvatar;

    String doctorId;
    String doctorName;
    String doctorAvatar;

    String serviceId;
    String serviceName;
}