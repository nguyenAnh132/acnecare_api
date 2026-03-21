package com.acnecare.api.admin.mapper;
import org.mapstruct.Mapper;

import com.acnecare.api.admin.dto.response.AdminProfileResponse;
import com.acnecare.api.admin.entity.AdminProfile;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    
    AdminProfileResponse toAdminProfileResponse(AdminProfile adminProfile);
    
}
