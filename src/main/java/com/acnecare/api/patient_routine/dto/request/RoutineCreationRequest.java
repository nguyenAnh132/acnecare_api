package com.acnecare.api.patient_routine.dto.request;

import com.acnecare.api.patient_routine.enums.TimeOfDay;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoutineCreationRequest {
    @NotBlank(message = "Tên Routine không được để trống")
    String routineName;

    String note;

    List<RoutineStepRequest> steps;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoutineStepRequest {
        @NotNull(message = "ID Sản phẩm không được để trống")
        String productId;

        @NotNull(message = "Vui lòng chọn mốc thời gian")
        TimeOfDay timeOfDay; // Sáng/Chiều/Tối

        Integer stepOrder;

        String notes; // Ghi chú bôi như thế nào
    }
}