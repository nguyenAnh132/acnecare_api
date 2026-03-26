package com.acnecare.api.brand.service;

import java.time.LocalDateTime;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import com.acnecare.api.brand.dto.request.AdminUpdateProfileBrandRequest;
import com.acnecare.api.brand.dto.request.BrandProfileUpdateRequest;
import com.acnecare.api.brand.dto.response.BrandProfileResponse;
import com.acnecare.api.brand.entity.BrandProfile;
import com.acnecare.api.brand.mapper.BrandProfileMapper;
import com.acnecare.api.brand.repository.BrandProfileRepository;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.common.helper.CurrentUserId;
import com.acnecare.api.user.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class BrandService {
    BrandProfileRepository brandProfileRepository;
    BrandProfileMapper brandProfileMapper;

    @PreAuthorize("hasAuthority('ROLE_BRAND')")
    public BrandProfileResponse getMyBrandProfile(){
        String userId = CurrentUserId.getCurrentUserId()
        .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        BrandProfile brandProfile = brandProfileRepository.findById(userId)
        .orElseThrow(() -> new AppException(ErrorCode.BRAND_PROFILE_NOT_FOUND));

        return brandProfileMapper.toBrandProfileResponse(brandProfile);
    } 

    public void createMyBrandProfile(User user) {
        String userId = user.getId();
        Optional<BrandProfile> alreadyExists = brandProfileRepository.findById(userId);
        if (alreadyExists.isPresent()) {
            throw new AppException(ErrorCode.BRAND_ALREADY_HAS_PROFILE);
        }
        
        BrandProfile brandProfile = new BrandProfile();
        brandProfile.setUser(user);
        LocalDateTime now = LocalDateTime.now();
        brandProfile.setCreatedAt(now);
        brandProfile.setUpdatedAt(now);
        brandProfileRepository.save(brandProfile);
    }

    @PreAuthorize("hasAuthority('ROLE_BRAND')")
    public BrandProfileResponse updateMyBrandProfile (BrandProfileUpdateRequest request){
        String userId = CurrentUserId.getCurrentUserId().
            orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        BrandProfile brandProfile = brandProfileRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.BRAND_PROFILE_NOT_FOUND));

        if (!brandProfile.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        brandProfileMapper.updateBrand(request, brandProfile);
        brandProfile.setUpdatedAt(LocalDateTime.now());
        return brandProfileMapper.toBrandProfileResponse(brandProfileRepository.save(brandProfile));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public BrandProfileResponse getBrandProfileById(String id){
        BrandProfile brandProfile = brandProfileRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.BRAND_PROFILE_NOT_FOUND));

        return brandProfileMapper.toBrandProfileResponse(brandProfile);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public BrandProfileResponse updateBrandProfileByAdmin(String id, AdminUpdateProfileBrandRequest request) {
        BrandProfile brandProfile = brandProfileRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.BRAND_PROFILE_NOT_FOUND));
        brandProfile.setVerificationStatus(request.getVerificationStatus());
        brandProfile.setRejectionReason(request.getRejectionReason());
        brandProfile.setUpdatedAt(LocalDateTime.now());
        return brandProfileMapper.toBrandProfileResponse(brandProfileRepository.save(brandProfile));
    }
}