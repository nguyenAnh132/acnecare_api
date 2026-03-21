package com.acnecare.api.patient.mapper;

import org.mapstruct.Mapper;
import com.acnecare.api.patient.dto.request.AcnePredictionDetailCreationRequest;
import com.acnecare.api.patient.dto.response.AcnePredictionDetailResponse;
import com.acnecare.api.patient.entity.AcnePredictionDetail;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AcnePredictionDetailMapper {
    AcnePredictionDetail toAcnePredictionDetail(AcnePredictionDetailCreationRequest request);
    AcnePredictionDetailResponse toAcnePredictionDetailResponse(AcnePredictionDetail acnePredictionDetail);
    List<AcnePredictionDetailResponse> toAcnePredictionDetailResponses(List<AcnePredictionDetailResponse> acnePredictionDetails);
}
