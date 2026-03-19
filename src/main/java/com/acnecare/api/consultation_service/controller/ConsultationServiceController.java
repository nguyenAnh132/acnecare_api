package com.acnecare.api.consultation_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.consultation_service.dto.request.ConsultationServiceCreationRequest;
import com.acnecare.api.consultation_service.dto.request.ConsultationServiceUpdateRequest;
import com.acnecare.api.consultation_service.dto.response.ConsultationServiceResponse;
import com.acnecare.api.consultation_service.service.ConsultationServiceService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/consultation-services")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsultationServiceController {

    ConsultationServiceService consultationServiceService;

    @PostMapping
    ApiResponse<ConsultationServiceResponse> createMyConsultationService(
            @RequestBody @Valid ConsultationServiceCreationRequest request
    ) {
        return ApiResponse.<ConsultationServiceResponse>builder()
                .code(1000)
                .message("Consultation service has been created successfully")
                .result(consultationServiceService.createMyConsultationService(request))
                .build();
    }

    @GetMapping("/me")
    ApiResponse<List<ConsultationServiceResponse>> getMyConsultationServices() {
        return ApiResponse.<List<ConsultationServiceResponse>>builder()
                .code(1000)
                .message("My consultation services have been retrieved successfully")
                .result(consultationServiceService.getMyConsultationServices())
                .build();
    }

    @GetMapping("/doctor/{doctorId}")
    ApiResponse<List<ConsultationServiceResponse>> getConsultationServicesByDoctorId(
            @PathVariable String doctorId
    ) {
        return ApiResponse.<List<ConsultationServiceResponse>>builder()
                .code(1000)
                .message("Consultation services have been retrieved successfully")
                .result(consultationServiceService.getConsultationServicesByDoctorId(doctorId))
                .build();
    }

    @GetMapping("/{serviceId}")
    ApiResponse<ConsultationServiceResponse> getConsultationServiceById(@PathVariable String serviceId) {
        return ApiResponse.<ConsultationServiceResponse>builder()
                .code(1000)
                .message("Consultation service has been retrieved successfully")
                .result(consultationServiceService.getConsultationServiceById(serviceId))
                .build();
    }

    @PutMapping("/{serviceId}")
    ApiResponse<ConsultationServiceResponse> updateMyConsultationService(
            @PathVariable String serviceId,
            @RequestBody @Valid ConsultationServiceUpdateRequest request
    ) {
        return ApiResponse.<ConsultationServiceResponse>builder()
                .code(1000)
                .message("Consultation service has been updated successfully")
                .result(consultationServiceService.updateMyConsultationService(serviceId, request))
                .build();
    }

    @DeleteMapping("/{serviceId}")
    ApiResponse<Void> deleteMyConsultationService(@PathVariable String serviceId) {
        consultationServiceService.deleteMyConsultationService(serviceId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Consultation service has been deleted successfully")
                .build();
    }
}
