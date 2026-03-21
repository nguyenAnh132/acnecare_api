package com.acnecare.api.patient.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import com.acnecare.api.patient.entity.PatientProfile;
import com.acnecare.api.patient.repository.PatientProfileRepository;
import com.acnecare.api.user.entity.User;
import com.acnecare.api.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import com.acnecare.api.patient.mapper.PatientProfileMapper;
import com.acnecare.api.patient.dto.response.PatientProfileResponse;
import java.time.LocalDateTime;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import org.springframework.security.core.context.SecurityContextHolder;
import com.acnecare.api.common.helper.CurrentUserId;
import com.acnecare.api.patient.dto.request.PatientProfileUpdateRequest;
import org.springframework.security.access.prepost.PreAuthorize;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PatientProfileService {

    UserRepository userRepository;
    PatientProfileRepository patientProfileRepository;
    PatientProfileMapper patientProfileMapper;

    public void createMyPatientProfile(User user) {
        var userId = CurrentUserId.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var isAlreadyExists = patientProfileRepository.existsByUserId(userId);
        if (isAlreadyExists) {
            throw new AppException(ErrorCode.PATIENT_PROFILE_ALREADY_EXISTS);
        }
        PatientProfile patientProfile = new PatientProfile();
        patientProfile.setUser(user);
        patientProfile.setCreatedAt(LocalDateTime.now());
        patientProfile.setUpdatedAt(LocalDateTime.now());

        patientProfileRepository.save(patientProfile);
    }

    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public PatientProfileResponse updateMyPatientProfile(PatientProfileUpdateRequest request) {
        var userId = CurrentUserId.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        var patientProfile = patientProfileRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PATIENT_PROFILE_NOT_FOUND));
        patientProfileMapper.updatePatientProfile(request, patientProfile);
        patientProfile.setUpdatedAt(LocalDateTime.now());
        return patientProfileMapper.toPatientProfileResponse(patientProfileRepository.save(patientProfile));
    }

    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    @Transactional(readOnly = true)
    public PatientProfileResponse getMyPatientProfile() {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var alreadyExists = patientProfileRepository.findById(user.getId());
        if (!alreadyExists.isPresent()) {
            throw new AppException(ErrorCode.PATIENT_PROFILE_NOT_FOUND);
        }
        return getPatientProfileById(userId);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional(readOnly = true)
    public PatientProfileResponse getPatientProfileById(String id) {
        PatientProfile patientProfile = patientProfileRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PATIENT_PROFILE_NOT_FOUND));

        return patientProfileMapper.toPatientProfileResponse(patientProfile);
    }

}
