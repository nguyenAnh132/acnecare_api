package com.acnecare.api.doctor.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorProfileStatusUpdateRequest {

    @NotBlank(message = "INVALID_STATUS")
    @Pattern(regexp = "^(PENDING|ACCEPTED|REJECTED)$", message = "INVALID_STATUS")
    String verificationStatus;

    // Sẽ dùng để ghi chú lý do nếu Admin chọn REJECTED
    String rejectionReason;
}