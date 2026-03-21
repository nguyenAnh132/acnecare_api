package com.acnecare.api.role.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import com.acnecare.api.role.service.RoleService;
import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.role.dto.request.RoleCreationRequest;
import com.acnecare.api.role.dto.response.RoleResponse;
import com.acnecare.api.role.dto.request.RoleUpdateRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleCreationRequest request) {
        return ApiResponse.<RoleResponse>builder()
            .code(1000)
            .message("Role created successfully")
            .result(roleService.createRole(request))
            .build();
    }

    @PutMapping("/{name}")
    ApiResponse<RoleResponse> updateRole(@PathVariable String name, @RequestBody RoleUpdateRequest request) {
        return ApiResponse.<RoleResponse>builder()
            .code(1000)
            .message("Role updated successfully")
            .result(roleService.updateRole(name, request))
            .build();
    }

    @GetMapping("/{name}")
    ApiResponse<RoleResponse> getRole(@PathVariable String name) {
        return ApiResponse.<RoleResponse>builder()
            .code(1000)
            .message("Role retrieved successfully")
            .result(roleService.getRole(name))
            .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
            .code(1000)
            .message("Roles retrieved successfully")
            .result(roleService.getAllRoles())
            .build();
    }

    @DeleteMapping("/{name}")
    ApiResponse<Void> deleteRole(@PathVariable String name) {
        roleService.deleteRole(name);
        return ApiResponse.<Void>builder()
            .code(1000)
            .message("Role deleted successfully")
            .build();
    }

}
