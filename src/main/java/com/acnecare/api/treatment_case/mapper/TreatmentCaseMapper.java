package com.acnecare.api.treatment_case.mapper;

import com.acnecare.api.treatment_case.dto.response.TreatmentCaseResponse;
import com.acnecare.api.treatment_case.entity.TreatmentCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TreatmentCaseMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", expression = "java((treatmentCase.getPatient().getFirstName() != null ? treatmentCase.getPatient().getFirstName() : \"\") + \" \" + (treatmentCase.getPatient().getLastName() != null ? treatmentCase.getPatient().getLastName() : \"\"))")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", expression = "java((treatmentCase.getDoctor().getFirstName() != null ? treatmentCase.getDoctor().getFirstName() : \"\") + \" \" + (treatmentCase.getDoctor().getLastName() != null ? treatmentCase.getDoctor().getLastName() : \"\"))")
    @Mapping(target = "consultations", source = "consultations")
    TreatmentCaseResponse toResponse(TreatmentCase treatmentCase);

    List<TreatmentCaseResponse> toResponseList(List<TreatmentCase> treatmentCases);
}
