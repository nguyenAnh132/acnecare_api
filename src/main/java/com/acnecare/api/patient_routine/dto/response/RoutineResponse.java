package com.acnecare.api.patient_routine.dto.response;

import com.acnecare.api.patient_routine.enums.TimeOfDay;
import com.acnecare.api.product.dto.response.ProductResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoutineResponse {
    String id;
    String routineName;
    String note;
    Boolean isActive;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<RoutineStepResponse> steps;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoutineStepResponse {
        String id;
        ProductResponse product; // Trả về full object Product luôn để hiển thị ảnh, tên...
        TimeOfDay timeOfDay;
        Integer stepOrder;
        String notes;
    }
}