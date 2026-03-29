package com.acnecare.api.treatment_case.dto.response;

import com.acnecare.api.product.dto.response.ProductResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreatmentPlanItemResponse {
    String id;
    String customName;
    Integer stepOrder;
    String timeSlot;
    String usageInstruction;
    Integer durationDays;
    ProductResponse product;
}