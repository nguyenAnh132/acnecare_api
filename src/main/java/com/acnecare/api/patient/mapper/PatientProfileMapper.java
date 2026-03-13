package com.acnecare.api.patient.mapper;

import org.mapstruct.Mapper;
import com.acnecare.api.patient.entity.PatientProfile;
import com.acnecare.api.patient.dto.response.PatientProfileResponse;
import com.acnecare.api.patient.dto.request.PatientProfileUpdateRequest;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientProfileMapper {
    void updatePatientProfile(PatientProfileUpdateRequest request, @MappingTarget PatientProfile patientProfile);
    
    PatientProfileResponse toPatientProfileResponse(PatientProfile patientProfile);

}
