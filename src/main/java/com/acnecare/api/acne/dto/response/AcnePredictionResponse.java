package com.acnecare.api.acne.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AcnePredictionResponse {
    String id;
    String patientId;
    String patientName;
    String doctorId;
    String doctorName;
    String severityLevel;
    String note;
    String imageBase64;
    LocalDateTime createdAt;
    List<AcneDetailResponse> details;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcneDetailResponse {
        String codeName;
        String name;
        int count;
    }
}