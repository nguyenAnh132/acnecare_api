package com.acnecare.api.consultation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.consultation.dto.request.ConsultationCreationRequest;
import com.acnecare.api.consultation.dto.response.ConsultationResponse;
import com.acnecare.api.consultation.service.ConsultationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/consultations")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ConsultationController {
    
    ConsultationService consultationService;
    
    @PostMapping
    public ApiResponse<ConsultationResponse> createConsultation(
        @Valid @RequestBody ConsultationCreationRequest request
    ) {
        return ApiResponse.<ConsultationResponse>builder()
            .code(1000)
            .message("Consultation created successfully")
            .result(consultationService.createConsultation(request))
            .build();
    }

    // lấy toàn bộ lịch sử khám
    @GetMapping("/case/{caseId}")
    public ApiResponse<List<ConsultationResponse>> getConsultationsByCase(
        @PathVariable String caseId)
    {
        return ApiResponse.<List<ConsultationResponse>>builder()
            .code(1000)
            .message("Consultations retrieved successfully")
            .result(consultationService.getConsultationsByCase(caseId))
            .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ConsultationResponse> getConsultationById(
        @PathVariable String id)
        {
            return ApiResponse.<ConsultationResponse>builder()
                .code(1000)
                .message("Consultation retrieved successfully")
                .result(consultationService.getConsultationById(id))
                .build();
        }
}
