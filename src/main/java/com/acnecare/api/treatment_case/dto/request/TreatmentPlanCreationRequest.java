package com.acnecare.api.treatment_case.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreatmentPlanCreationRequest {
    String notes;
    List<PlanItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlanItemRequest {
        String productId;
        String customName;
        Integer stepOrder;
        String timeSlot;
        String usageInstruction;
        Integer durationDays;
    }
}