package com.acnecare.api.consultation.service;

import com.acnecare.api.appointment.entity.Appointment;
import com.acnecare.api.appointment.enums.AppointmentStatus;
import com.acnecare.api.appointment.repository.AppointmentRepository;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.common.helper.CurrentUserId;
import com.acnecare.api.consultation.dto.request.ConsultationCreationRequest;
import com.acnecare.api.consultation.dto.response.ConsultationResponse;
import com.acnecare.api.consultation.entity.Consultation;
import com.acnecare.api.consultation.mapper.ConsultationMapper;
import com.acnecare.api.consultation.repository.ConsultationRepository;
import com.acnecare.api.treatment_case.entity.TreatmentCase;
import com.acnecare.api.treatment_case.repository.TreatmentCaseRepository;
import com.acnecare.api.user.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsultationService {

    AppointmentRepository appointmentRepository;
    TreatmentCaseRepository treatmentCaseRepository;
    ConsultationRepository consultationRepository;
    ConsultationMapper consultationMapper;

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @Transactional
    public ConsultationResponse createConsultation(ConsultationCreationRequest request) {
        String doctorId = CurrentUserId.getCurrentUserId().orElseThrow();

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        // Kiểm tra đúng bác sĩ của appointment
        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        if (!appointment.getStatus().equals(AppointmentStatus.COMPLETED.name())) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_COMPLETED_FOR_CONSULTATION);
        }

        if (consultationRepository.existsByAppointmentId(request.getAppointmentId())) {
            throw new AppException(ErrorCode.CONSULTATION_ALREADY_EXISTS_FOR_APPOINTMENT);
        }

        User doctor = appointment.getDoctor();
        User patient = appointment.getPatient();

        boolean isNewCase = false;
        TreatmentCase treatmentCase = treatmentCaseRepository
                .findByPatientIdAndDoctorIdAndStatus(patient.getId(), doctorId, "ACTIVE")
                .orElse(null);

        if (treatmentCase == null) {
            treatmentCase = TreatmentCase.builder()
                    .patient(patient)
                    .doctor(doctor)
                    .chiefComplaint(request.getChiefComplaint())
                    .build();
            treatmentCase = treatmentCaseRepository.save(treatmentCase);
            isNewCase = true;
            log.info("Tạo TreatmentCase mới cho bệnh nhân {} với bác sĩ {}", patient.getId(), doctorId);
        } else {
            log.info("Dùng TreatmentCase có sẵn {} cho bệnh nhân {}", treatmentCase.getId(), patient.getId());
        }

        Consultation consultation = Consultation.builder()
                .treatmentCase(treatmentCase)
                .doctor(doctor)
                .appointment(appointment)
                .assessment(request.getAssessment())
                .planSummary(request.getPlanSummary())
                .doctorNotes(request.getDoctorNotes())
                .build();

        consultation = consultationRepository.save(consultation);

        ConsultationResponse response = consultationMapper.toResponse(consultation);
        response.setNewCase(isNewCase);
        return response;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_PATIENT')")
    public List<ConsultationResponse> getConsultationsByCase(String caseId) {
        String currentUserId = CurrentUserId.getCurrentUserId().orElseThrow();

        TreatmentCase treatmentCase = treatmentCaseRepository.findById(caseId)
                .orElseThrow(() -> new AppException(ErrorCode.TREATMENT_CASE_NOT_FOUND));

        boolean isDoctor = treatmentCase.getDoctor().getId().equals(currentUserId);
        boolean isPatient = treatmentCase.getPatient().getId().equals(currentUserId);

        if (!isDoctor && !isPatient) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        return consultationMapper.toResponseList(
                consultationRepository.findByTreatmentCaseIdOrderByConsultationAtDesc(caseId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_PATIENT')")
    public ConsultationResponse getConsultationById(String id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CONSULTATION_NOT_FOUND));
        return consultationMapper.toResponse(consultation);
    }
}
