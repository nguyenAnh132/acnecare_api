package com.acnecare.api.patient.mapper;

import org.mapstruct.Mapper;
import com.acnecare.api.patient.dto.request.PatientProfileCreationRequest;
import com.acnecare.api.patient.entity.PatientProfile;
import com.acnecare.api.patient.dto.response.PatientProfileResponse;

@Mapper(componentModel = "spring")
public interface PatientProfileMapper {

    PatientProfile toPatientProfile(PatientProfileCreationRequest request);

    PatientProfileResponse toPatientProfileResponse(PatientProfile patientProfile);

}
