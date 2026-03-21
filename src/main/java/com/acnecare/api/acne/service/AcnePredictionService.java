package com.acnecare.api.acne.service;

import com.acnecare.api.acne.entity.Acne;
import com.acnecare.api.acne.repository.AcneRepository;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.acne.dto.request.AcnePredictionCreationRequest;
import com.acnecare.api.acne.dto.response.AcnePredictionResponse;
import com.acnecare.api.acne.entity.AcnePrediction;
import com.acnecare.api.acne.entity.AcnePredictionDetail;
import com.acnecare.api.acne.mapper.AcnePredictionMapper;
import com.acnecare.api.acne.repository.AcnePredictionRepository;
import com.acnecare.api.user.entity.User;
import com.acnecare.api.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AcnePredictionService {

    AcnePredictionRepository acnePredictionRepository;
    UserRepository userRepository;
    AcneRepository acneRepository;
    AcnePredictionMapper acnePredictionMapper;

    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_PATIENT', 'ROLE_DOCTOR')")
    public AcnePredictionResponse createAcnePrediction(AcnePredictionCreationRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        AcnePrediction prediction = acnePredictionMapper.toAcnePrediction(request);
        prediction.setCreatedAt(LocalDateTime.now());

        boolean isDoctor = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("DOCTOR") || r.getName().equals("ROLE_DOCTOR"));

        if (isDoctor) {
            if (request.getPatientId() == null || request.getPatientId().isBlank()) {
                throw new AppException(ErrorCode.PATIENT_ID_REQUIRED);
            }
            User patient = userRepository.findById(request.getPatientId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            prediction.setDoctor(currentUser);
            prediction.setPatient(patient);
        } else {
            prediction.setPatient(currentUser);
        }

        List<AcnePredictionDetail> details = new ArrayList<>();
        if (request.getDetails() != null) {
            for (var detailReq : request.getDetails()) {
                Acne acne = acneRepository.findByCodeName(detailReq.getClassName())
                        .orElseGet(() -> acneRepository.save(
                                Acne.builder()
                                        .codeName(detailReq.getClassName())
                                        .name(translateToVn(detailReq.getClassName()))
                                        .createdAt(LocalDateTime.now())
                                        .build()));

                details.add(AcnePredictionDetail.builder()
                        .acnePrediction(prediction)
                        .acne(acne)
                        .count(detailReq.getCount())
                        .build());
            }
        }
        prediction.setDetails(details);

        AcnePrediction savedPrediction = acnePredictionRepository.save(prediction);
        return acnePredictionMapper.toAcnePredictionResponse(savedPrediction);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_PATIENT', 'ROLE_DOCTOR', 'ROLE_ADMIN')")
    public List<AcnePredictionResponse> getPatientScanHistory(String patientId) {
        return acnePredictionRepository.findByPatientIdOrderByCreatedAtDesc(patientId)
                .stream()
                .map(acnePredictionMapper::toAcnePredictionResponse)
                .toList();
    }

    private String translateToVn(String className) {
        return switch (className.toLowerCase().trim()) {
            case "dark spot" -> "Vết thâm";
            case "blackheads" -> "Mụn đầu đen";
            case "whiteheads" -> "Mụn đầu trắng";
            case "nodules" -> "Mụn bọc";
            case "papules" -> "Mụn sẩn";
            case "pustules" -> "Mụn mủ";
            default -> className;
        };
    }
}