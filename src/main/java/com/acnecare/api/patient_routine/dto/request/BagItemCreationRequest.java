package com.acnecare.api.patient_routine.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BagItemCreationRequest {
    @NotBlank(message = "ID Sản phẩm không được để trống")
    String productId;
}