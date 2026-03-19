package com.acnecare.api.doctorschedule.dto.response;

import java.time.LocalDateTime;

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
public class DoctorScheduleResponse {

    String id;
    String doctorId;
    String consultationServiceId;
    String consultationServiceName;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String status;
    String note;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}