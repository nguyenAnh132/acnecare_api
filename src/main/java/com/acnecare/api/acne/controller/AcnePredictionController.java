package com.acnecare.api.acne.controller;

import com.acnecare.api.acne.dto.request.AcnePredictionCreationRequest;
import com.acnecare.api.acne.dto.response.AcnePredictionResponse;
import com.acnecare.api.acne.service.AcnePredictionService;
import com.acnecare.api.common.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/acne-predictions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AcnePredictionController {

    AcnePredictionService acnePredictionService;

    // 1. API: Lưu kết quả phân tích AI (Bác sĩ hoặc Bệnh nhân tự lưu)
    @PostMapping
    public ApiResponse<AcnePredictionResponse> savePrediction(@RequestBody AcnePredictionCreationRequest request) {
        return ApiResponse.<AcnePredictionResponse>builder()
                .code(1000)
                .message("Lưu kết quả phân tích AI thành công")
                .result(acnePredictionService.createAcnePrediction(request))
                .build();
    }

    // 2. API: Lấy lịch sử quét da của một bệnh nhân cụ thể
    @GetMapping("/patient/{patientId}")
    public ApiResponse<List<AcnePredictionResponse>> getPatientScanHistory(@PathVariable String patientId) {
        return ApiResponse.<List<AcnePredictionResponse>>builder()
                .code(1000)
                .message("Lấy lịch sử phân tích da thành công")
                .result(acnePredictionService.getPatientScanHistory(patientId))
                .build();
    }
}