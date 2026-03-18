package com.acnecare.api.doctorschedule.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class DoctorScheduleUpdateRequest {

    @NotNull(message = "INVALID_START_TIME")
    @Future(message = "INVALID_START_TIME")
    LocalDateTime startTime;

    @NotNull(message = "INVALID_END_TIME")
    @Future(message = "INVALID_END_TIME")
    LocalDateTime endTime;

    String consultationServiceId;

    @Size(max = 500, message = "INVALID_NOTE")
    String note;

    @NotNull(message = "INVALID_STATUS")
    @Size(max = 30, message = "INVALID_STATUS")
    String status;
}