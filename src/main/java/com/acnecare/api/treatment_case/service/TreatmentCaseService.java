package com.acnecare.api.treatment_case.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.treatment_case.dto.response.TreatmentCaseResponse;
import com.acnecare.api.treatment_case.mapper.TreatmentCaseMapper;
import com.acnecare.api.treatment_case.repository.TreatmentCaseRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TreatmentCaseService {

    TreatmentCaseRepository treatmentCaseRepository;
    TreatmentCaseMapper treatmentCaseMapper;

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public List<TreatmentCaseResponse> getCasesByPatient(String patientId) {
        return treatmentCaseMapper
                .toResponseList(treatmentCaseRepository.findByPatientIdOrderByCreatedAtDesc(patientId));

    }

    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public List<TreatmentCaseResponse> getMyCases() {
        String patientId = SecurityContextHolder.getContext().getAuthentication().getName();
        return treatmentCaseMapper
                .toResponseList(treatmentCaseRepository.findByPatientIdOrderByCreatedAtDesc(patientId));
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_PATIENT')")
    @Transactional(readOnly = true) // THÊM DÒNG NÀY LÀ QUAN TRỌNG NHẤT
    public TreatmentCaseResponse getCaseById(String caseId) {
        return treatmentCaseMapper.toResponse(
                treatmentCaseRepository.findById(caseId)
                        .orElseThrow(() -> new AppException(ErrorCode.TREATMENT_CASE_NOT_FOUND)));
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @Transactional(readOnly = true)
    public List<TreatmentCaseResponse> getCasesByDoctor() {
        String doctorId = SecurityContextHolder.getContext().getAuthentication().getName();
        return treatmentCaseMapper.toResponseList(treatmentCaseRepository.findByDoctorIdOrderByUpdatedAtDesc(doctorId));
    }
}
