package com.acnecare.api.consultation_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.acnecare.api.consultation_service.dto.request.ConsultationServiceCreationRequest;
import com.acnecare.api.consultation_service.dto.request.ConsultationServiceUpdateRequest;
import com.acnecare.api.consultation_service.dto.response.ConsultationServiceResponse;
import com.acnecare.api.consultation_service.entity.ConsultationService;

@Mapper(componentModel = "spring")
public interface ConsultationServiceMapper {

    @Mapping(source = "doctor.id", target = "doctorId")
    ConsultationServiceResponse toConsultationServiceResponse(ConsultationService consultationService);

    ConsultationService toConsultationService(ConsultationServiceCreationRequest request);

    void updateConsultationService(
            ConsultationServiceUpdateRequest request,
            @MappingTarget ConsultationService consultationService
    );
}