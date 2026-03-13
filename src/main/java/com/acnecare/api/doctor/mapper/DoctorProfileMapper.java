package com.acnecare.api.doctor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.acnecare.api.doctor.dto.request.DoctorProfileCreationRequest;
import com.acnecare.api.doctor.dto.request.DoctorProfileUpdateRequest;
import com.acnecare.api.doctor.entity.DoctorProfile;
import com.acnecare.api.doctor.dto.response.DoctorProfileResponse;

@Mapper(componentModel = "spring")
public interface DoctorProfileMapper {
    DoctorProfile toDoctorProfile(DoctorProfileCreationRequest request);

    DoctorProfileResponse toDoctorProfileResponse(DoctorProfile doctorProfile);

    void updateDoctorProfile(DoctorProfileUpdateRequest request, @MappingTarget DoctorProfile doctorProfile);
}
