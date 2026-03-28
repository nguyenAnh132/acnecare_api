package com.acnecare.api.treatment_case.controller;

import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.treatment_case.dto.response.TreatmentCaseResponse;
import com.acnecare.api.treatment_case.service.TreatmentCaseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treatment-cases")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TreatmentCaseController {

    TreatmentCaseService treatmentCaseService;

    @GetMapping("/patient/{patientId}")
    public ApiResponse<List<TreatmentCaseResponse>> getCasesByPatient(@PathVariable String patientId) {
        return ApiResponse.<List<TreatmentCaseResponse>>builder()
                .code(1000)
                .message("Lấy danh sách hồ sơ điều trị thành công!")
                .result(treatmentCaseService.getCasesByPatient(patientId))
                .build();
    }

    @GetMapping("/my-cases")
    public ApiResponse<List<TreatmentCaseResponse>> getMyCases() {
        return ApiResponse.<List<TreatmentCaseResponse>>builder()
                .code(1000)
                .message("Lấy hồ sơ điều trị của bạn thành công!")
                .result(treatmentCaseService.getMyCases())
                .build();
    }

    // Lấy chi tiết 1 case (kèm consultations)
    @GetMapping("/{caseId}")
    public ApiResponse<TreatmentCaseResponse> getCaseById(@PathVariable String caseId) {
        return ApiResponse.<TreatmentCaseResponse>builder()
                .code(1000)
                .message("Lấy chi tiết hồ sơ điều trị thành công!")
                .result(treatmentCaseService.getCaseById(caseId))
                .build();
    }
}
