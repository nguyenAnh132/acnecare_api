package com.acnecare.api.role.mapper;

import org.mapstruct.Mapper;
import com.acnecare.api.role.dto.request.RoleCreationRequest;
import com.acnecare.api.role.dto.response.RoleResponse;
import com.acnecare.api.role.entity.Role;
import java.util.List;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.acnecare.api.role.dto.request.RoleUpdateRequest;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreationRequest request);

    RoleResponse toRoleResponse(Role role);

    List<RoleResponse> toRoleResponseList(List<Role> roles);

    @Mapping(target = "permissions", ignore = true)
    void updateRole(RoleUpdateRequest request, @MappingTarget Role role);

}
