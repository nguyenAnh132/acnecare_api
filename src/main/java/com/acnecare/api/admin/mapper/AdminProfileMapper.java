package com.acnecare.api.admin.mapper;
import org.mapstruct.Mapper;

import com.acnecare.api.admin.dto.response.AdminProfileResponse;
import com.acnecare.api.admin.entity.AdminProfile;
import com.acnecare.api.admin.dto.request.AdminProfileUpdateRequest;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AdminProfileMapper {

    void updateAdminProfile(AdminProfileUpdateRequest request, @MappingTarget AdminProfile adminProfile);
    
    AdminProfileResponse toAdminProfileResponse(AdminProfile adminProfile);
    
}
