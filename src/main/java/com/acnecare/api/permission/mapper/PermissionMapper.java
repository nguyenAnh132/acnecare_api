package com.acnecare.api.permission.mapper;

import org.mapstruct.Mapper;
import com.acnecare.api.permission.dto.request.PermissionCreationRequest;
import com.acnecare.api.permission.dto.response.PermissionResponse;
import com.acnecare.api.permission.entity.Permission;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionCreationRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
    List<PermissionResponse> toPermissionResponseList(List<Permission> permissions);

}
