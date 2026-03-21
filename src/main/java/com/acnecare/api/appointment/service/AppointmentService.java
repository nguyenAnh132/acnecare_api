package com.acnecare.api.appointment.service;

import com.acnecare.api.appointment.dto.request.AppointmentCreationRequest;
import com.acnecare.api.appointment.dto.request.AppointmentReviewRequest;
import com.acnecare.api.appointment.dto.request.AppointmentUpdateRequest;
import com.acnecare.api.appointment.dto.response.AppointmentResponse;
import com.acnecare.api.appointment.dto.response.WsMessageResponse;
import com.acnecare.api.appointment.entity.Appointment;
import com.acnecare.api.appointment.enums.AppointmentMode;
import com.acnecare.api.appointment.enums.AppointmentStatus;
import com.acnecare.api.appointment.mapper.AppointmentMapper;
import com.acnecare.api.appointment.repository.AppointmentRepository;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.consultation_service.repository.ConsultationServiceRepository;
import com.acnecare.api.user.entity.User;
import com.acnecare.api.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppointmentService {

    AppointmentRepository appointmentRepository;
    UserRepository userRepository;
    AppointmentMapper appointmentMapper;
    SimpMessagingTemplate messagingTemplate;
    ConsultationServiceRepository consultationServiceRepository;

    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public AppointmentResponse createAppointment(AppointmentCreationRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User patient = userRepository.findById(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        User doctor = userRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean isDoctor = doctor.getRoles().stream()
                .anyMatch(role -> role.getName().equals("DOCTOR"));
        if (!isDoctor) {
            throw new AppException(ErrorCode.USER_IS_NOT_DOCTOR);
        }

        if (appointmentRepository.existsByDoctorIdAndAppointmentTimeAndStatusNotIn(
                doctor.getId(),
                request.getAppointmentTime(),
                List.of("REJECTED", "CANCELLED"))) {
            throw new AppException(ErrorCode.APPOINTMENT_TIME_UNAVAILABLE);
        }

        try {
            AppointmentMode.valueOf(request.getMode().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.ERROR_INVALID_MODE);
        }

        Appointment appointment = appointmentMapper.toAppointment(request);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setStatus(AppointmentStatus.PENDING.name());
        appointment.setPaymentStatus("UNPAID");
        appointment.setCreatedAt(LocalDateTime.now());
        var consultationService = consultationServiceRepository
                .findByIdAndDoctorId(request.getServiceId(), doctor.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CONSULTATION_SERVICE_NOT_FOUND));

        // 2. Gán thông tin dịch vụ vào lịch khám
        appointment.setServiceId(consultationService.getId());
        appointment.setServiceName(consultationService.getServiceName());
        appointment = appointmentRepository.save(appointment);

        WsMessageResponse doctorMsg = WsMessageResponse.builder()
                .type("NEW_APPOINTMENT")
                .message("Bạn có lịch khám mới từ " + patient.getFirstName() + " " + patient.getLastName())
                .data(appointment.getId())
                .build();
        messagingTemplate.convertAndSend("/topic/doctor/" + doctor.getId() + "/notifications", doctorMsg);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:00");
        String formattedTime = request.getAppointmentTime().format(formatter);

        Map<String, Object> wsData = new HashMap<>();
        wsData.put("time", formattedTime);
        wsData.put("patientId", patient.getId());

        WsMessageResponse patientMsg = WsMessageResponse.builder()
                .type("SLOT_TAKEN")
                .message("Khung giờ " + formattedTime + " vừa có người đặt!")
                .data(wsData)
                .build();
        messagingTemplate.convertAndSend("/topic/doctor/" + doctor.getId() + "/schedule", patientMsg);

        log.info("Bắn WebSocket thành công cho Bác sĩ {} và vô hiệu hóa khung giờ {}", doctor.getId(), formattedTime);

        return appointmentMapper.toAppointmentResponse(appointment);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public AppointmentResponse updateAppointmentStatus(String id, AppointmentUpdateRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !appointment.getDoctor().getId().equals(currentUserId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        if (request.getStatus() != null) {
            try {
                AppointmentStatus.valueOf(request.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new AppException(ErrorCode.ERROR_INVALID_STATUS);
            }
        }

        appointmentMapper.updateAppointment(appointment, request);

        if (request.getMeetingUrl() != null && !request.getMeetingUrl().trim().isEmpty()) {
            appointment.setMeetingUrl(request.getMeetingUrl());
        }
        appointment = appointmentRepository.save(appointment);

        AppointmentResponse responseDto = appointmentMapper.toAppointmentResponse(appointment);

        WsMessageResponse patientMsg = WsMessageResponse.builder()
                .type("STATUS_UPDATED")
                .message("Lịch khám ngày "
                        + appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                        + " đã chuyển thành: " + appointment.getStatus())
                .data(responseDto)
                .build();
        messagingTemplate.convertAndSend("/topic/patient/" + appointment.getPatient().getId() + "/notifications",
                patientMsg);
        if (appointment.getStatus().equals("REJECTED")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:00");
            Map<String, Object> wsData = new HashMap<>();
            wsData.put("time", appointment.getAppointmentTime().format(formatter));

            WsMessageResponse freeSlotMsg = WsMessageResponse.builder()
                    .type("SLOT_FREED")
                    .message("Khung giờ đã được trống!")
                    .data(wsData)
                    .build();
            messagingTemplate.convertAndSend("/topic/doctor/" + appointment.getDoctor().getId() + "/schedule",
                    freeSlotMsg);
        }

        return responseDto;
    }

    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public List<AppointmentResponse> getMyPatientAppointments() {
        String patientId = SecurityContextHolder.getContext().getAuthentication().getName();
        return appointmentMapper.toAppointmentResponseList(
                appointmentRepository.findByPatientIdOrderByAppointmentTimeDesc(patientId));
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public List<AppointmentResponse> getMyDoctorAppointments() {
        String doctorId = SecurityContextHolder.getContext().getAuthentication().getName();
        return appointmentMapper.toAppointmentResponseList(
                appointmentRepository.findByDoctorIdOrderByAppointmentTimeDesc(doctorId));
    }

    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public AppointmentResponse cancelAppointment(String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        if (!appointment.getPatient().getId().equals(currentUserId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        String currentStatus = appointment.getStatus();
        if (currentStatus.equals("COMPLETED") || currentStatus.equals("CANCELLED")
                || currentStatus.equals("REJECTED")) {

            throw new AppException(ErrorCode.APPOINTMENT_CANNOT_BE_CANCELLED);
        }

        appointment.setStatus("CANCELLED");
        appointment = appointmentRepository.save(appointment);

        WsMessageResponse doctorMsg = WsMessageResponse.builder()
                .type("APPOINTMENT_CANCELLED")
                .message("Bệnh nhân " + appointment.getPatient().getFirstName() + " đã hủy lịch khám.")
                .data(appointment.getId())
                .build();
        messagingTemplate.convertAndSend("/topic/doctor/" + appointment.getDoctor().getId() + "/notifications",
                doctorMsg);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:00");
        Map<String, Object> wsData = new HashMap<>();
        wsData.put("time", appointment.getAppointmentTime().format(formatter));

        WsMessageResponse patientMsg = WsMessageResponse.builder()
                .type("SLOT_FREED")
                .message("Khung giờ đã được trống!")
                .data(wsData)
                .build();
        messagingTemplate.convertAndSend("/topic/doctor/" + appointment.getDoctor().getId() + "/schedule", patientMsg);

        return appointmentMapper.toAppointmentResponse(appointment);
    }

    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public AppointmentResponse reviewAppointment(String id, AppointmentReviewRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        String patientId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        if (!appointment.getStatus().equals(AppointmentStatus.COMPLETED.name())) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_COMPLETED);
        }

        if (appointment.getRating() != null) {
            throw new AppException(ErrorCode.APPOINTMENT_ALREADY_REVIEWED);
        }

        appointment.setRating(request.getRating());
        appointment.setReview(request.getReview());
        appointment = appointmentRepository.save(appointment);

        return appointmentMapper.toAppointmentResponse(appointment);
    }

    public List<String> getBusyTimes(String doctorId, java.time.LocalDate date) {
        java.time.LocalDateTime startOfDay = date.atStartOfDay();
        java.time.LocalDateTime endOfDay = date.atTime(23, 59, 59);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:00");

        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, startOfDay, endOfDay)
                .stream()
                .filter(a -> !a.getStatus().equals("REJECTED") && !a.getStatus().equals("CANCELLED"))
                .map(a -> a.getAppointmentTime().format(formatter))
                .toList();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_PATIENT', 'ROLE_DOCTOR', 'ROLE_ADMIN')")
    public AppointmentResponse getAppointmentById(String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin
                && !appointment.getPatient().getId().equals(currentUserId)
                && !appointment.getDoctor().getId().equals(currentUserId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        return appointmentMapper.toAppointmentResponse(appointment);
    }
}