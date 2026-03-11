package com.acnecare.api.permission.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import com.acnecare.api.permission.service.PermissionService;
import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.permission.dto.request.PermissionCreationRequest;
import com.acnecare.api.permission.dto.response.PermissionResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionCreationRequest request) {
        return ApiResponse.<PermissionResponse>builder()
            .code(1000)
            .message("Permission created successfully")
            .result(permissionService.createPermission(request))
            .build();
    }


    @GetMapping("/{id}")
    ApiResponse<PermissionResponse> getPermission(@PathVariable String id) {
        return ApiResponse.<PermissionResponse>builder()
            .code(1000)
            .message("Permission retrieved successfully")
            .result(permissionService.getPermission(id))
            .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
            .code(1000)
            .message("Permissions retrieved successfully")
            .result(permissionService.getAllPermissions())
            .build();
    }
    

    @DeleteMapping("/{id}")
    ApiResponse<Void> deletePermission(@PathVariable String id) {
        permissionService.deletePermission(id);
        return ApiResponse.<Void>builder()
            .code(1000)
            .message("Permission deleted successfully")
            .build();
    }

}
