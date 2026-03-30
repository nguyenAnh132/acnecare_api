package com.acnecare.api.treatment_case.controller;

import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.treatment_case.dto.request.TreatmentPlanCreationRequest;
import com.acnecare.api.treatment_case.service.TreatmentPlanService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/treatment-plans")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TreatmentPlanController {

    TreatmentPlanService planService;

    @PostMapping("/case/{caseId}")
    public ApiResponse<String> createPlan(
            @PathVariable String caseId,
            @RequestBody TreatmentPlanCreationRequest request) {
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Thành công")
                .result(planService.createPlan(caseId, request))
                .build();
    }

    @PostMapping("/{planId}/apply-routine")
    public ApiResponse<String> applyPlanToRoutine(@PathVariable String planId) {
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Thành công")
                .result(planService.applyPlanToMyRoutine(planId))
                .build();
    }
    @PutMapping("/{planId}")
    public ApiResponse<String> updatePlan(
            @PathVariable String planId,
            @RequestBody TreatmentPlanCreationRequest request) {
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Thành công")
                .result(planService.updatePlan(planId, request))
                .build();
    }
}