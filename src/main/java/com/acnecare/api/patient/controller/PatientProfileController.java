package com.acnecare.api.patient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.patient.dto.request.PatientProfileCreationRequest;
import com.acnecare.api.patient.dto.response.PatientProfileResponse;
import com.acnecare.api.patient.service.PatientProfileService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/patient-profiles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PatientProfileController {
    PatientProfileService patientProfileService;


    @GetMapping
    ApiResponse<PatientProfileResponse> getMyPatientProfile() {
        ApiResponse<PatientProfileResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(patientProfileService.getMyPatientProfile());
        return apiResponse;
    }

    @PostMapping
    ApiResponse<PatientProfileResponse> createMyPatientProfile(@RequestBody PatientProfileCreationRequest request) {
        return ApiResponse.<PatientProfileResponse>builder()
            .code(1000)
            .message("Patient Profile has been created successfully")
            .result(patientProfileService.createMyPatientProfile(request))
            .build();
    }
    
}
