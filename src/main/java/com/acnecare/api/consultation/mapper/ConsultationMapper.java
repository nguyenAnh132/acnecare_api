package com.acnecare.api.consultation.mapper;

import com.acnecare.api.consultation.dto.response.ConsultationResponse;
import com.acnecare.api.consultation.entity.Consultation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConsultationMapper {

    @Mapping(target = "caseId", source = "treatmentCase.id")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", expression = "java((consultation.getDoctor().getFirstName() != null ? consultation.getDoctor().getFirstName() : \"\") + \" \" + (consultation.getDoctor().getLastName() != null ? consultation.getDoctor().getLastName() : \"\"))")
    @Mapping(target = "appointmentId", source = "appointment.id")
    @Mapping(target = "isNewCase", ignore = true)
    ConsultationResponse toResponse(Consultation consultation);

    List<ConsultationResponse> toResponseList(List<Consultation> consultations);
}
