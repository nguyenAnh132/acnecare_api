package com.acnecare.api.appointment.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentCreationRequest {

    @NotBlank(message = "Vui lòng chọn Bác sĩ!")
    String doctorId;

    @NotNull(message = "Vui lòng chọn thời gian khám!")
    @Future(message = "Thời gian khám phải ở tương lai!")
    LocalDateTime appointmentTime;

    @NotBlank(message = "Vui lòng chọn hình thức khám (ONLINE/OFFLINE)!")
    String mode;

    @NotBlank(message = "Vui lòng chọn phương thức thanh toán!")
    String paymentMethod;
    @NotBlank(message = "Vui lòng chọn dịch vụ khám!")
    String serviceId;

    String note;
}