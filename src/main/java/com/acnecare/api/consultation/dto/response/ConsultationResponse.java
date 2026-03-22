package com.acnecare.api.consultation.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationResponse {

    String id;
    String caseId;         // ID của treatment case
    String doctorId;
    String doctorName;
    String appointmentId;
    LocalDateTime consultationAt;
    String assessment;
    String planSummary;
    String doctorNotes;
    LocalDateTime createdAt;
    boolean isNewCase;     // để frontend biết case vừa được tạo mới
}
