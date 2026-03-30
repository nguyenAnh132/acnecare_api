package com.acnecare.api.treatment_case.mapper;

import com.acnecare.api.product.mapper.ProductMapper;
import com.acnecare.api.treatment_case.dto.response.TreatmentPlanItemResponse;
import com.acnecare.api.treatment_case.dto.response.TreatmentPlanResponse;
import com.acnecare.api.treatment_case.entity.TreatmentPlan;
import com.acnecare.api.treatment_case.entity.TreatmentPlanItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface TreatmentPlanMapper {
    TreatmentPlanResponse toResponse(TreatmentPlan treatmentPlan);

    List<TreatmentPlanResponse> toResponseList(List<TreatmentPlan> treatmentPlans);

    TreatmentPlanItemResponse toItemResponse(TreatmentPlanItem item);
}