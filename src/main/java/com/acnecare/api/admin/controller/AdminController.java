package com.acnecare.api.admin.controller;

import java.time.LocalDate;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acnecare.api.admin.dto.response.AdminProfileResponse;
import com.acnecare.api.admin.dto.response.UserAdminResponse;
import com.acnecare.api.admin.dto.request.AdminProfileUpdateRequest;
import com.acnecare.api.admin.service.AdminService;
import com.acnecare.api.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminController {

    AdminService adminService;

//     @GetMapping("/users")
//     @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//     public ApiResponse<Page<UserAdminResponse>> getUsers(
//             Pageable pageable,
//             @RequestParam(required = false) String search,
//             @RequestParam(required = false) String role,
//             @RequestParam(required = false) String status,
//             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//             @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
//             @RequestParam(required = false, defaultValue = "desc") String sortDir) {

//         log.info("REST request to get users list for admin: page={}, size={}, search={}, role={}, status={}, startDate={}, endDate={}, sortBy={}, sortDir={}",
//                 pageable.getPageNumber(), pageable.getPageSize(), search, role, status, startDate, endDate, sortBy, sortDir);
//         Page<UserAdminResponse> users = adminService.getUsers(pageable, search, role, status, startDate, endDate, sortBy, sortDir);
//         return ApiResponse.<Page<UserAdminResponse>>builder()
//                 .code(1000)
//                 .message("Users retrieved successfully")
//                 .result(users)
//                 .build();
//     }

    @GetMapping("/profile/me")
    ApiResponse<AdminProfileResponse> getMyAdminProfile() {
        return ApiResponse.<AdminProfileResponse>builder()
                .code(1000)
                .message("Admin Profile has been retrieved successfully")
                .result(adminService.getMyAdminProfile())
                .build();
    }

    @GetMapping("/profile/{id}")
    ApiResponse<AdminProfileResponse> getAdminProfileById(String id) {
        return ApiResponse.<AdminProfileResponse>builder()
                .code(1000)
                .message("Admin Profile has been retrieved successfully")
                .result(adminService.getAdminProfileById(id))
                .build();
    }

    @PutMapping("/profile/me")
    ApiResponse<AdminProfileResponse> updateMyAdminProfile(AdminProfileUpdateRequest request) {
        return ApiResponse.<AdminProfileResponse>builder()
                .code(1000)
                .message("Admin Profile has been updated successfully")
                .result(adminService.updateMyAdminProfile(request))
                .build();
    }

}