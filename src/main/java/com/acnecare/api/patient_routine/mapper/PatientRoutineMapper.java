package com.acnecare.api.patient_routine.mapper;

import com.acnecare.api.patient_routine.dto.request.RoutineCreationRequest;
import com.acnecare.api.patient_routine.dto.response.RoutineResponse;
import com.acnecare.api.patient_routine.entity.DailyRoutine;
import com.acnecare.api.patient_routine.entity.DailyRoutineStep;
import com.acnecare.api.product.mapper.ProductMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface PatientRoutineMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "steps", ignore = true)
    DailyRoutine toDailyRoutine(RoutineCreationRequest request);

    RoutineResponse toRoutineResponse(DailyRoutine routine);

    List<RoutineResponse> toRoutineResponses(List<DailyRoutine> routines);

    RoutineResponse.RoutineStepResponse toRoutineStepResponse(DailyRoutineStep step);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "steps", ignore = true)
    void updateDailyRoutine(RoutineCreationRequest request, @MappingTarget DailyRoutine routine);
}