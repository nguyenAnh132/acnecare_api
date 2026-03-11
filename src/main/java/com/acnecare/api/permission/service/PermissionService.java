package com.acnecare.api.permission.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;

import com.acnecare.api.permission.repository.PermissionRepository;
import com.acnecare.api.permission.entity.Permission;
import com.acnecare.api.permission.dto.request.PermissionCreationRequest;
import com.acnecare.api.permission.dto.response.PermissionResponse;
import com.acnecare.api.permission.mapper.PermissionMapper;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public PermissionResponse createPermission(PermissionCreationRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public PermissionResponse getPermission(String id) {
        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        return permissionMapper.toPermissionResponse(permission);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissionMapper.toPermissionResponseList(permissions);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deletePermission(String id) {
        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        permissionRepository.delete(permission);
    }
}
