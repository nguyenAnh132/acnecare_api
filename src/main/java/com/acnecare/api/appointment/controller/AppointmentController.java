package com.acnecare.api.appointment.controller;

import com.acnecare.api.appointment.dto.request.AppointmentCreationRequest;
import com.acnecare.api.appointment.dto.request.AppointmentReviewRequest;
import com.acnecare.api.appointment.dto.request.AppointmentUpdateRequest;
import com.acnecare.api.appointment.dto.response.AppointmentResponse;
import com.acnecare.api.appointment.service.AppointmentService;
import com.acnecare.api.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentController {

        AppointmentService appointmentService;

        @PostMapping
        public ApiResponse<AppointmentResponse> createAppointment(
                        @Valid @RequestBody AppointmentCreationRequest request) {
                return ApiResponse.<AppointmentResponse>builder()
                                .code(1000)
                                .message("Đặt lịch khám thành công. Vui lòng chờ Bác sĩ xác nhận!")
                                .result(appointmentService.createAppointment(request))
                                .build();
        }

        @PutMapping("/{id}/status")
        public ApiResponse<AppointmentResponse> updateAppointmentStatus(
                        @PathVariable String id,
                        @Valid @RequestBody AppointmentUpdateRequest request) {

                return ApiResponse.<AppointmentResponse>builder()
                                .code(1000)
                                .message("Cập nhật trạng thái lịch khám thành công!")
                                .result(appointmentService.updateAppointmentStatus(id, request))
                                .build();
        }

        @GetMapping("/my-patient-history")
        public ApiResponse<List<AppointmentResponse>> getMyPatientAppointments() {
                return ApiResponse.<List<AppointmentResponse>>builder()
                                .code(1000)
                                .message("Lấy lịch sử khám bệnh thành công!")
                                .result(appointmentService.getMyPatientAppointments())
                                .build();
        }

        @GetMapping("/my-doctor-schedule")
        public ApiResponse<List<AppointmentResponse>> getMyDoctorAppointments() {
                return ApiResponse.<List<AppointmentResponse>>builder()
                                .code(1000)
                                .message("Lấy lịch làm việc của Bác sĩ thành công!")
                                .result(appointmentService.getMyDoctorAppointments())
                                .build();
        }

        @PutMapping("/{id}/cancel")
        public ApiResponse<AppointmentResponse> cancelAppointment(@PathVariable String id) {
                return ApiResponse.<AppointmentResponse>builder()
                                .code(1000)
                                .message("Hủy lịch khám thành công!")
                                .result(appointmentService.cancelAppointment(id))
                                .build();
        }

        @PostMapping("/{id}/review")
        public ApiResponse<AppointmentResponse> reviewAppointment(
                        @PathVariable String id,
                        @Valid @RequestBody AppointmentReviewRequest request) {

                return ApiResponse.<AppointmentResponse>builder()
                                .code(1000)
                                .message("Cảm ơn bạn đã gửi đánh giá!")
                                .result(appointmentService.reviewAppointment(id, request))
                                .build();
        }

        @GetMapping("/doctor/{doctorId}/busy-times")
        public ApiResponse<List<String>> getBusyTimes(
                        @PathVariable String doctorId,
                        @RequestParam java.time.LocalDate date) {
                return ApiResponse.<List<String>>builder()
                                .code(1000)
                                .result(appointmentService.getBusyTimes(doctorId, date))
                                .build();
        }

        @GetMapping("/{id}")
        public ApiResponse<AppointmentResponse> getAppointmentById(@PathVariable String id) {
                return ApiResponse.<AppointmentResponse>builder()
                                .code(1000)
                                .message("Lấy thông tin chi tiết thành công!")
                                .result(appointmentService.getAppointmentById(id))
                                .build();
        }
}