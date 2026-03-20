package com.acnecare.api.patient.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.acnecare.api.patient.dto.request.AcnePredictionCreationRequest;
import com.acnecare.api.patient.dto.request.AcnePredictionUpdateRequest;
import com.acnecare.api.patient.dto.response.AcnePredictionResponse;
import com.acnecare.api.patient.entity.AcnePrediction;

@Mapper(componentModel = "spring")
public interface AcnePredictionMapper {
    AcnePrediction toAcnePrediction(AcnePredictionCreationRequest request);

    void updateAcnePrediction( @MappingTarget AcnePrediction acnePrediction, AcnePredictionUpdateRequest request);

    AcnePredictionResponse toAcnePredictionResponse(AcnePrediction acnePrediction);
    
    List<AcnePredictionResponse> toAcnePredictionResponses(List<AcnePrediction> acnePredictions);
}

