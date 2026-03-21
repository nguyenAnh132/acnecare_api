package com.acnecare.api.acne.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AcnePredictionCreationRequest {
    String patientId;
    String severityLevel;
    String note;
    String imageBase64;
    List<AcneDetailRequest> details;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcneDetailRequest {
        String className;
        int count;
    }
}