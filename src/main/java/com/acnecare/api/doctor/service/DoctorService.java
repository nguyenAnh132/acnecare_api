package com.acnecare.api.doctor.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.common.helper.CurrentUserId;
import com.acnecare.api.doctor.dto.request.DoctorProfileUpdateRequest;
import com.acnecare.api.doctor.dto.response.DoctorProfileResponse;
import com.acnecare.api.doctor.entity.DoctorProfile;
import com.acnecare.api.doctor.mapper.DoctorProfileMapper;
import com.acnecare.api.doctor.repository.DoctorProfileRepository;
import com.acnecare.api.user.entity.User;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DoctorService {

    DoctorProfileRepository doctorProfileRepository;
    DoctorProfileMapper doctorProfileMapper;

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public DoctorProfileResponse getMyDoctorProfile() {
        var userId = CurrentUserId.getCurrentUserId()
            .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var doctorProfile = doctorProfileRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.DOCTOR_PROFILE_NOT_FOUND));

        return doctorProfileMapper.toDoctorProfileResponse(doctorProfile);
    }

    public void createMyDoctorProfile(User user) {
        var userId = user.getId();
        var alreadyExists = doctorProfileRepository.findById(userId);
        if (alreadyExists.isPresent()) {
            throw new AppException(ErrorCode.DOCTOR_ALREADY_HAS_PROFILE);
        }
        
        DoctorProfile doctorProfile = new DoctorProfile();
        doctorProfile.setUser(user);
        doctorProfile.setVerificationStatus("PENDING");
        doctorProfile.setRatingAvg(0.0);
        doctorProfile.setRatingCount(0);
        doctorProfile.setRejectionReason(null);
      
        doctorProfileRepository.save(doctorProfile);
        
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public DoctorProfileResponse updateMyDoctorProfile(DoctorProfileUpdateRequest request) {
        var userId = CurrentUserId.getCurrentUserId()
            .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var doctorProfile = doctorProfileRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.DOCTOR_PROFILE_NOT_FOUND));

        if (!doctorProfile.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        doctorProfileMapper.updateDoctorProfile(request, doctorProfile);
        return doctorProfileMapper.toDoctorProfileResponse(doctorProfileRepository.save(doctorProfile));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DoctorProfileResponse getDoctorProfileById(String id) {
        var doctorProfile = doctorProfileRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.DOCTOR_PROFILE_NOT_FOUND));

        return doctorProfileMapper.toDoctorProfileResponse(doctorProfile);
    }
}
