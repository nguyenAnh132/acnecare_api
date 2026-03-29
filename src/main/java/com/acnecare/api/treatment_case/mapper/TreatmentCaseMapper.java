package com.acnecare.api.treatment_case.mapper;

import com.acnecare.api.consultation.mapper.ConsultationMapper; // THÊM IMPORT NÀY
import com.acnecare.api.treatment_case.dto.response.TreatmentCaseResponse;
import com.acnecare.api.treatment_case.entity.TreatmentCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

// THÊM uses = {ConsultationMapper.class} VÀO ĐÂY ĐỂ NÓ BIẾT CÁCH MAP CONSULTATION
@Mapper(componentModel = "spring", uses = { ConsultationMapper.class, TreatmentPlanMapper.class })
public interface TreatmentCaseMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", expression = "java(treatmentCase.getPatient() != null ? (treatmentCase.getPatient().getFirstName() != null ? treatmentCase.getPatient().getFirstName() : \"\") + \" \" + (treatmentCase.getPatient().getLastName() != null ? treatmentCase.getPatient().getLastName() : \"\") : \"Khách vãng lai\")")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", expression = "java(treatmentCase.getDoctor() != null ? (treatmentCase.getDoctor().getFirstName() != null ? treatmentCase.getDoctor().getFirstName() : \"\") + \" \" + (treatmentCase.getDoctor().getLastName() != null ? treatmentCase.getDoctor().getLastName() : \"\") : \"Chưa rõ\")")
    @Mapping(target = "consultations", source = "consultations")
    @Mapping(target = "treatmentPlans", source = "treatmentPlans")
    TreatmentCaseResponse toResponse(TreatmentCase treatmentCase);

    List<TreatmentCaseResponse> toResponseList(List<TreatmentCase> treatmentCases);
}