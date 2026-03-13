package com.acnecare.api.patient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.patient.dto.response.PatientProfileResponse;
import com.acnecare.api.patient.service.PatientProfileService;
import com.acnecare.api.patient.dto.request.PatientProfileUpdateRequest;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PatientProfileController {
    PatientProfileService patientProfileService;

    @PutMapping
    ApiResponse<PatientProfileResponse> updatePatientProfile(@RequestBody @Valid PatientProfileUpdateRequest request) {
        return ApiResponse.<PatientProfileResponse>builder()
                .code(1000)
                .message("Patient Profile has been updated successfully")
                .result(patientProfileService.updateMyPatientProfile(request))
                .build();
    }


    @GetMapping
    ApiResponse<PatientProfileResponse> getMyPatientProfile() {
        return ApiResponse.<PatientProfileResponse>builder()
                .code(1000)
                .message("Patient Profile has been retrieved successfully")
                .result(patientProfileService.getMyPatientProfile())
                .build();
    }


    @GetMapping("/{id}")
    ApiResponse<PatientProfileResponse> getPatientProfileById(@PathVariable String id) {
        return ApiResponse.<PatientProfileResponse>builder()
                .code(1000)
                .message("Patient Profile has been retrieved successfully")
                .result(patientProfileService.getPatientProfileById(id))
                .build();
    }

}
