package com.acnecare.api.patient.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acnecare.api.common.helper.CurrentUserId;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.patient.dto.request.AcnePredictionCreationRequest;
import com.acnecare.api.patient.dto.request.AcnePredictionUpdateRequest;
import com.acnecare.api.patient.dto.response.AcnePredictionResponse;
import com.acnecare.api.patient.entity.AcnePrediction;
import com.acnecare.api.patient.entity.PatientProfile;
import com.acnecare.api.patient.mapper.AcnePredictionMapper;
import com.acnecare.api.patient.repository.AcnePredictionRepository;
import com.acnecare.api.patient.repository.PatientProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AcnePredictionService {

    AcnePredictionRepository acnePredictionRepository;
    AcnePredictionMapper acnePredictionMapper;
    PatientProfileRepository patientProfileRepository;

    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public AcnePredictionResponse createAcnePrediction(AcnePredictionCreationRequest request) {

        var userId = CurrentUserId.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        PatientProfile patientProfile = patientProfileRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PATIENT_PROFILE_NOT_FOUND));

        AcnePrediction acnePrediction = acnePredictionMapper.toAcnePrediction(request);
        acnePrediction.setCreatedAt(LocalDateTime.now());
        acnePrediction.setUpdatedAt(LocalDateTime.now());
        acnePrediction.setPatient(patientProfile);

        return acnePredictionMapper
            .toAcnePredictionResponse(acnePredictionRepository.save(acnePrediction));
    }


    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    @Transactional(readOnly = true)
    public List<AcnePredictionResponse> getAllAcnePredictions() {
        var userId = CurrentUserId.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        
        PatientProfile patientProfile = patientProfileRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PATIENT_PROFILE_NOT_FOUND));
        
        var acnePredictions = acnePredictionRepository.findAllByPatient(patientProfile);
        
        return acnePredictionMapper.toAcnePredictionResponses(acnePredictions);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR') or hasAnyAuthority('ROLE_PATIENT')")
    @Transactional(readOnly = true)
    public AcnePredictionResponse getAcnePredictionById(String id) {
        var userId = CurrentUserId.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        AcnePrediction acnePrediction = acnePredictionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACNE_PREDICTION_NOT_FOUND));
        if (!acnePrediction.getPatient().getId().equals(userId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        return acnePredictionMapper.toAcnePredictionResponse(acnePrediction);
    }


    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public AcnePredictionResponse updateAcnePrediction(String id, AcnePredictionUpdateRequest request) {
        var userId = CurrentUserId.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        AcnePrediction acnePrediction = acnePredictionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACNE_PREDICTION_NOT_FOUND));
        if (!acnePrediction.getPatient().getId().equals(userId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        
        acnePredictionMapper.updateAcnePrediction(acnePrediction, request);
        acnePrediction.setUpdatedAt(LocalDateTime.now());
        return acnePredictionMapper.toAcnePredictionResponse(acnePredictionRepository.save(acnePrediction));
    }


    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public void deleteAcnePrediction(String id) {
        var userId = CurrentUserId.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        AcnePrediction acnePrediction = acnePredictionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACNE_PREDICTION_NOT_FOUND));
        if (!acnePrediction.getPatient().getId().equals(userId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        acnePredictionRepository.delete(acnePrediction);
    }

}
