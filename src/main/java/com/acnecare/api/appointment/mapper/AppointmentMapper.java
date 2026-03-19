package com.acnecare.api.appointment.mapper;

import com.acnecare.api.appointment.dto.request.AppointmentCreationRequest;
import com.acnecare.api.appointment.dto.request.AppointmentUpdateRequest;
import com.acnecare.api.appointment.dto.response.AppointmentResponse;
import com.acnecare.api.appointment.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Appointment toAppointment(AppointmentCreationRequest request);

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", expression = "java((appointment.getPatient().getFirstName() != null ? appointment.getPatient().getFirstName() : \"\") + \" \" + (appointment.getPatient().getLastName() != null ? appointment.getPatient().getLastName() : \"\"))")
    @Mapping(target = "patientAvatar", source = "patient.avatarUrl")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", expression = "java((appointment.getDoctor().getFirstName() != null ? appointment.getDoctor().getFirstName() : \"\") + \" \" + (appointment.getDoctor().getLastName() != null ? appointment.getDoctor().getLastName() : \"\"))")
    @Mapping(target = "doctorAvatar", source = "doctor.avatarUrl")
    AppointmentResponse toAppointmentResponse(Appointment appointment);

    List<AppointmentResponse> toAppointmentResponseList(List<Appointment> appointments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "appointmentTime", ignore = true)
    void updateAppointment(@MappingTarget Appointment appointment, AppointmentUpdateRequest request);
}