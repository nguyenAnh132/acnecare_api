package com.acnecare.api.acne.mapper;

import com.acnecare.api.acne.dto.request.AcnePredictionCreationRequest;
import com.acnecare.api.acne.dto.response.AcnePredictionResponse;
import com.acnecare.api.acne.entity.AcnePrediction;
import com.acnecare.api.acne.entity.AcnePredictionDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AcnePredictionMapper {

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "details", ignore = true)
    AcnePrediction toAcnePrediction(AcnePredictionCreationRequest request);

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", expression = "java(acnePrediction.getPatient().getFirstName() + \" \" + acnePrediction.getPatient().getLastName())")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", expression = "java(acnePrediction.getDoctor() != null ? acnePrediction.getDoctor().getFirstName() + \" \" + acnePrediction.getDoctor().getLastName() : null)")
    AcnePredictionResponse toAcnePredictionResponse(AcnePrediction acnePrediction);

    @Mapping(target = "codeName", source = "acne.codeName")
    @Mapping(target = "name", source = "acne.name")
    AcnePredictionResponse.AcneDetailResponse toAcneDetailResponse(AcnePredictionDetail detail);

    List<AcnePredictionResponse> toAcnePredictionResponses(List<AcnePrediction> predictions);
}