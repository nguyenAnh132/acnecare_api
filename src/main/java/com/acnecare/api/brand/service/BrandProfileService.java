package com.acnecare.api.brand.service;

import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties.Apiversion.Use;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.acnecare.api.brand.dto.request.BrandProfileCreationRequest;
import com.acnecare.api.brand.dto.response.BrandProfileResponse;
import com.acnecare.api.brand.entity.BrandProfile;
import com.acnecare.api.brand.mapper.BrandProfileMapper;
import com.acnecare.api.brand.repository.BrandProfileRepository;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.user.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class BrandProfileService {
    UserRepository userRepository;
    BrandProfileRepository brandProfileRepository;
    BrandProfileMapper brandProfileMapper;

    public BrandProfileResponse getBrandProfileById(String id) {
        BrandProfile brandProfile = brandProfileRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.BRAND_PROFILE_NOT_FOUND));

        return brandProfileMapper.toBrandProfileResponse(brandProfile);
    }

    public BrandProfileResponse getMyBrandProfile() {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var alreadyExists = brandProfileRepository.findById(user.getId());
        if (!alreadyExists.isPresent()) {
            throw new AppException(ErrorCode.BRAND_PROFILE_NOT_FOUND);
        }
        return getBrandProfileById(userId);
    }

    @PreAuthorize("hasAuthority('ROLE_BRAND')")
    public BrandProfileResponse createBrandProfileResponse(BrandProfileCreationRequest request) {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();

        var user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var alreadyExists = brandProfileRepository.findById(userId);
        if (alreadyExists.isPresent()) {
            throw new AppException(ErrorCode.BRAND_ALREADY_HAS_PROFILE);
        }

        BrandProfile brandProfile = brandProfileMapper.toBrandProfile(request);
        brandProfile.setUser(user);

        return brandProfileMapper
            .toBrandProfileResponse(brandProfileRepository.save(brandProfile));
    }

    @PreAuthorize("hasAuthority('ROLE_BRAND')")
    public BrandProfileResponse updateBrandProfile(BrandProfileCreationRequest request) {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();

        var brandProfile = brandProfileRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.BRAND_PROFILE_NOT_FOUND));

        if (!brandProfile.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        brandProfileMapper.updateBrand(request, brandProfile);

        return brandProfileMapper.toBrandProfileResponse(brandProfileRepository.save(brandProfile));
    }
}
