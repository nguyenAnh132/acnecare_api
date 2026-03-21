package com.acnecare.api.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acnecare.api.admin.dto.response.AdminProfileResponse;
import com.acnecare.api.admin.service.AdminService;
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
public class AdminController {

    AdminService adminService;

    @GetMapping
    ApiResponse<AdminProfileResponse> getMyAdminProfile() {
        return ApiResponse.<AdminProfileResponse>builder()
                .code(1000)
                .message("Admin Profile has been retrieved successfully")
                .result(adminService.getMyAdminProfile())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<AdminProfileResponse> getAdminProfileById(String id) {
        return ApiResponse.<AdminProfileResponse>builder()
                .code(1000)
                .message("Admin Profile has been retrieved successfully")
                .result(adminService.getAdminProfileById(id))
                .build();
    }

    @PutMapping
    ApiResponse<AdminProfileResponse> updateMyAdminProfile() {
        return ApiResponse.<AdminProfileResponse>builder()
                .code(1000)
                .message("Admin Profile has been updated successfully")
                .result(adminService.updateMyAdminProfile())
                .build();
    }

}