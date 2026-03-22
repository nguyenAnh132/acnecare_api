package com.acnecare.api.patient_routine.controller;

import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.patient_routine.dto.request.RoutineCreationRequest;
import com.acnecare.api.patient_routine.dto.request.BagItemCreationRequest; // <-- ĐÃ ĐỔI IMPORT
import com.acnecare.api.patient_routine.dto.response.RoutineResponse;
import com.acnecare.api.patient_routine.service.PatientRoutineService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient-routines")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientRoutineController {

    PatientRoutineService routineService;

    // ==========================================
    // A. API CHO TÚI ĐỒ (BAG)
    // ==========================================

    @PostMapping("/bag")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ApiResponse<Void> addProductToBag(@RequestBody @Valid BagItemCreationRequest request) { // <-- ĐÃ ĐỔI THAM SỐ
        routineService.addProductToBag(request);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Sản phẩm đã được thêm vào túi đồ cá nhân")
                .build();
    }

    // ==========================================
    // B. API CHO LỊCH TRÌNH CHĂM SÓC DA (DAILY ROUTINE)
    // ==========================================

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ApiResponse<RoutineResponse> createMyRoutine(@RequestBody @Valid RoutineCreationRequest request) {
        return ApiResponse.<RoutineResponse>builder()
                .code(1000)
                .message("Tạo lịch trình chăm sóc da thành công")
                .result(routineService.createMyRoutine(request))
                .build();
    }

    @PutMapping("/{routineId}")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ApiResponse<RoutineResponse> updateMyRoutine(
            @PathVariable String routineId,
            @RequestBody @Valid RoutineCreationRequest request) {
        return ApiResponse.<RoutineResponse>builder()
                .code(1000)
                .message("Cập nhật lịch trình thành công")
                .result(routineService.updateMyRoutine(routineId, request))
                .build();
    }

    @DeleteMapping("/{routineId}")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ApiResponse<Void> deleteMyRoutine(@PathVariable String routineId) {
        routineService.deleteMyRoutine(routineId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Xóa lịch trình thành công")
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ApiResponse<List<RoutineResponse>> getMyRoutines() {
        return ApiResponse.<List<RoutineResponse>>builder()
                .code(1000)
                .message("Lấy danh sách lịch trình thành công")
                .result(routineService.getMyRoutines())
                .build();
    }
}