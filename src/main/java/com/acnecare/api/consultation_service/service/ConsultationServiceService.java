package com.acnecare.api.consultation_service.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.common.helper.CurrentUserId;
import com.acnecare.api.consultation_service.dto.request.ConsultationServiceCreationRequest;
import com.acnecare.api.consultation_service.dto.request.ConsultationServiceUpdateRequest;
import com.acnecare.api.consultation_service.dto.response.ConsultationServiceResponse;
import com.acnecare.api.consultation_service.entity.ConsultationService;
import com.acnecare.api.consultation_service.mapper.ConsultationServiceMapper;
import com.acnecare.api.consultation_service.repository.ConsultationServiceRepository;
import com.acnecare.api.doctor.repository.DoctorProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsultationServiceService {

    ConsultationServiceRepository consultationServiceRepository;
    ConsultationServiceMapper consultationServiceMapper;
    DoctorProfileRepository doctorProfileRepository;

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ConsultationServiceResponse createMyConsultationService(ConsultationServiceCreationRequest request) {
        var userId = CurrentUserId.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var doctorProfile = doctorProfileRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.DOCTOR_PROFILE_NOT_FOUND));

        ConsultationService consultationService = consultationServiceMapper.toConsultationService(request);
        consultationService.setDoctor(doctorProfile);

        return consultationServiceMapper.toConsultationServiceResponse(
                consultationServiceRepository.save(consultationService)
        );
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public List<ConsultationServiceResponse> getMyConsultationServices() {
        var userId = CurrentUserId.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        doctorProfileRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.DOCTOR_PROFILE_NOT_FOUND));

        return consultationServiceRepository.findByDoctorId(userId).stream()
                .map(consultationServiceMapper::toConsultationServiceResponse)
                .toList();
    }

    public List<ConsultationServiceResponse> getConsultationServicesByDoctorId(String doctorId) {
        doctorProfileRepository.findById(doctorId)
                .orElseThrow(() -> new AppException(ErrorCode.DOCTOR_PROFILE_NOT_FOUND));

        return consultationServiceRepository.findByDoctorIdAndIsActiveTrue(doctorId).stream()
                .map(consultationServiceMapper::toConsultationServiceResponse)
                .toList();
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ConsultationServiceResponse updateMyConsultationService(
            String serviceId,
            ConsultationServiceUpdateRequest request
    ) {
        var userId = CurrentUserId.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var consultationService = consultationServiceRepository.findByIdAndDoctorId(serviceId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.CONSULTATION_SERVICE_NOT_FOUND));

        consultationServiceMapper.updateConsultationService(request, consultationService);

        return consultationServiceMapper.toConsultationServiceResponse(
                consultationServiceRepository.save(consultationService)
        );
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public void deleteMyConsultationService(String serviceId) {
        var userId = CurrentUserId.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var consultationService = consultationServiceRepository.findByIdAndDoctorId(serviceId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.CONSULTATION_SERVICE_NOT_FOUND));

        consultationServiceRepository.delete(consultationService);
    }

    public ConsultationServiceResponse getConsultationServiceById(String serviceId) {
        var consultationService = consultationServiceRepository.findById(serviceId)
                .orElseThrow(() -> new AppException(ErrorCode.CONSULTATION_SERVICE_NOT_FOUND));

        return consultationServiceMapper.toConsultationServiceResponse(consultationService);
    }
}