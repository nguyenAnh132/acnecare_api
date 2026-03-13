package com.acnecare.api.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acnecare.api.admin.dto.response.AdminProfileResponse;
import com.acnecare.api.admin.service.AdminProfileService;
import com.acnecare.api.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminProfileController {

    AdminProfileService adminProfileService;

    @GetMapping
    ApiResponse<AdminProfileResponse> getMyAdminProfile() {
        return ApiResponse.<AdminProfileResponse>builder()
                .code(1000)
                .message("Admin Profile has been retrieved successfully")
                .result(adminProfileService.getMyAdminProfile())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<AdminProfileResponse> getAdminProfileById(String id) {
        return ApiResponse.<AdminProfileResponse>builder()
                .code(1000)
                .message("Admin Profile has been retrieved successfully")
                .result(adminProfileService.getAdminProfileById(id))
                .build();
    }

    @PutMapping
    ApiResponse<AdminProfileResponse> updateMyAdminProfile() {
        return ApiResponse.<AdminProfileResponse>builder()
                .code(1000)
                .message("Admin Profile has been updated successfully")
                .result(adminProfileService.updateMyAdminProfile())
                .build();
    }

}