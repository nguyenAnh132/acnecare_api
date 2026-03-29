package com.acnecare.api.treatment_case.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.acnecare.api.consultation.dto.response.ConsultationResponse;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreatmentCaseResponse {

    String id;
    String patientId;
    String patientName;
    String doctorId;
    String doctorName;
    LocalDate startDate;
    LocalDate endDate;
    String status;
    String chiefComplaint;
    String doctorNote;
    List<ConsultationResponse> consultations;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
