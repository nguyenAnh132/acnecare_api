package com.acnecare.api.consultation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationCreationRequest {

    @NotBlank(message = "Appointment ID is required")
    String appointmentId;

    @NotBlank(message = "Assessment is required")
    @Size(max = 2000, message = "Assessment must not exceed 2000 characters")
    String assessment;

    @Size(max = 2000, message = "Plan summary must not exceed 2000 characters")
    String planSummary;

    @Size(max = 1000, message = "Doctor notes must not exceed 1000 characters")
    String doctorNotes;

    @Size(max = 500, message = "Chief complaint must not exceed 500 characters")
    String chiefComplaint;
}
