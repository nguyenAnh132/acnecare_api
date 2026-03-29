package com.acnecare.api.role.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import com.acnecare.api.role.reposity.RoleReposity;
import com.acnecare.api.role.mapper.RoleMapper;
import com.acnecare.api.role.dto.request.RoleCreationRequest;
import com.acnecare.api.role.dto.response.RoleResponse;
import com.acnecare.api.role.entity.Role;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import java.util.List;
import com.acnecare.api.role.dto.request.RoleUpdateRequest;
import org.springframework.security.access.prepost.PreAuthorize;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleReposity roleRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleCreationRequest request) {
        Role role = roleMapper.toRole(request);
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public RoleResponse updateRole(String name, RoleUpdateRequest request) {
        Role role = roleRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        roleMapper.updateRole(request, role);
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public RoleResponse getRole(String name) {
        Role role = roleRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        return roleMapper.toRoleResponse(role);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toRoleResponseList(roles);
    }

    public void deleteRole(String name) {
        Role role = roleRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        roleRepository.delete(role);
    }

}
