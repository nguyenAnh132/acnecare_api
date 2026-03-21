package com.acnecare.api.doctor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.doctor.dto.request.DoctorProfileUpdateRequest;
import com.acnecare.api.doctor.dto.response.DoctorProfileResponse;
import com.acnecare.api.doctor.service.DoctorService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DoctorController {

    DoctorService doctorProfileService;

    @PutMapping("/profile/me")
    ApiResponse<DoctorProfileResponse> updateMyDoctorProfile(@RequestBody @Valid DoctorProfileUpdateRequest request) {
        return ApiResponse.<DoctorProfileResponse>builder()
                .code(1000)
                .message("Doctor Profile has been updated successfully")
                .result(doctorProfileService.updateMyDoctorProfile(request))
                .build();
    }

    @GetMapping("/profile/me")
    ApiResponse<DoctorProfileResponse> getMyDoctorProfile() {
        return ApiResponse.<DoctorProfileResponse>builder()
                .code(1000)
                .message("Doctor Profile has been retrieved successfully")
                .result(doctorProfileService.getMyDoctorProfile())
                .build();
    }

    @GetMapping("/profile/{id}")
    ApiResponse<DoctorProfileResponse> getDoctorProfileById(@PathVariable String id) {
        return ApiResponse.<DoctorProfileResponse>builder()
                .code(1000)
                .message("Doctor Profile has been retrieved successfully")
                .result(doctorProfileService.getDoctorProfileById(id))
                .build();
    }
}
