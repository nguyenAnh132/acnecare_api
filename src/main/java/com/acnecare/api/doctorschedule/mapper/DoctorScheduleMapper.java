package com.acnecare.api.doctorschedule.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.acnecare.api.doctorschedule.dto.request.DoctorScheduleCreationRequest;
import com.acnecare.api.doctorschedule.dto.request.DoctorScheduleUpdateRequest;
import com.acnecare.api.doctorschedule.dto.response.DoctorScheduleResponse;
import com.acnecare.api.doctorschedule.entity.DoctorSchedule;

@Mapper(componentModel = "spring")
public interface DoctorScheduleMapper {

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "consultationService.id", target = "consultationServiceId")
    @Mapping(source = "consultationService.serviceName", target = "consultationServiceName")
    DoctorScheduleResponse toDoctorScheduleResponse(DoctorSchedule doctorSchedule);

    DoctorSchedule toDoctorSchedule(DoctorScheduleCreationRequest request);

    void updateDoctorSchedule(
            DoctorScheduleUpdateRequest request,
            @MappingTarget DoctorSchedule doctorSchedule
    );
}