package com.acnecare.api.treatment_case.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreatmentPlanResponse {
    String id;
    String notes;
    LocalDateTime createdAt;
    List<TreatmentPlanItemResponse> items;
}