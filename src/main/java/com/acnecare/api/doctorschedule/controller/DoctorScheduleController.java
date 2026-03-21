package com.acnecare.api.doctorschedule.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.doctorschedule.dto.request.DoctorScheduleCreationRequest;
import com.acnecare.api.doctorschedule.dto.request.DoctorScheduleUpdateRequest;
import com.acnecare.api.doctorschedule.dto.response.DoctorScheduleResponse;
import com.acnecare.api.doctorschedule.service.DoctorScheduleService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/doctor-schedules")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DoctorScheduleController {

        DoctorScheduleService doctorScheduleService;

        @PostMapping
        ApiResponse<DoctorScheduleResponse> createMySchedule(
                        @RequestBody @Valid DoctorScheduleCreationRequest request) {
                return ApiResponse.<DoctorScheduleResponse>builder()
                                .code(1000)
                                .message("Doctor schedule has been created successfully")
                                .result(doctorScheduleService.createMySchedule(request))
                                .build();
        }

        @GetMapping("/me")
        ApiResponse<List<DoctorScheduleResponse>> getMySchedules() {
                return ApiResponse.<List<DoctorScheduleResponse>>builder()
                                .code(1000)
                                .message("Doctor schedules have been retrieved successfully")
                                .result(doctorScheduleService.getMySchedules())
                                .build();
        }

        @GetMapping("/doctor/{doctorId}")
        ApiResponse<List<DoctorScheduleResponse>> getSchedulesByDoctorId(
                        @PathVariable String doctorId,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                        @RequestParam(required = false) String serviceId // THÊM DÒNG NÀY
        ) {
                return ApiResponse.<List<DoctorScheduleResponse>>builder()
                                .code(1000)
                                .message("Doctor schedules have been retrieved successfully")
                                .result(doctorScheduleService.getSchedulesByDoctorId(doctorId, serviceId, date))
                                .build();
        }

        @GetMapping("/{scheduleId}")
        ApiResponse<DoctorScheduleResponse> getScheduleById(@PathVariable String scheduleId) {
                return ApiResponse.<DoctorScheduleResponse>builder()
                                .code(1000)
                                .message("Doctor schedule has been retrieved successfully")
                                .result(doctorScheduleService.getScheduleById(scheduleId))
                                .build();
        }

        @PutMapping("/{scheduleId}")
        ApiResponse<DoctorScheduleResponse> updateMySchedule(
                        @PathVariable String scheduleId,
                        @RequestBody @Valid DoctorScheduleUpdateRequest request) {
                return ApiResponse.<DoctorScheduleResponse>builder()
                                .code(1000)
                                .message("Doctor schedule has been updated successfully")
                                .result(doctorScheduleService.updateMySchedule(scheduleId, request))
                                .build();
        }

        @DeleteMapping("/{scheduleId}")
        ApiResponse<Void> deleteMySchedule(@PathVariable String scheduleId) {
                doctorScheduleService.deleteMySchedule(scheduleId);
                return ApiResponse.<Void>builder()
                                .code(1000)
                                .message("Doctor schedule has been deleted successfully")
                                .build();
        }
}