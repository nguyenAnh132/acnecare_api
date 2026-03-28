package com.acnecare.api.admin.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

import com.acnecare.api.admin.entity.AdminProfile;
import com.acnecare.api.admin.repository.AdminRepository;
import com.acnecare.api.user.entity.User;
import com.acnecare.api.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import com.acnecare.api.admin.mapper.AdminMapper;    
import com.acnecare.api.admin.dto.response.AdminProfileResponse;

import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;

import org.springframework.security.core.context.SecurityContextHolder;

import com.acnecare.api.common.helper.CurrentUserId;

import org.springframework.security.access.prepost.PreAuthorize;

@Service

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminService {

    private final UserRepository userRepository = null;
    private final AdminRepository adminProfileRepository = null;
    private final AdminMapper adminMapper = null;

    public void createMyAdminProfile(User user) {

        var userId = CurrentUserId.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var isAlreadyExists = adminProfileRepository.existsById(userId);

        if (isAlreadyExists) {
            throw new AppException(ErrorCode.ADMIN_PROFILE_ALREADY_EXISTS);
        }

        AdminProfile adminProfile = new AdminProfile();

        adminProfile.setUser(user);
       

        adminProfileRepository.save(adminProfile);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public AdminProfileResponse getMyAdminProfile() {

        var userId = SecurityContextHolder.getContext().getAuthentication().getName();

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var alreadyExists = adminProfileRepository.findById(user.getId());

        if (!alreadyExists.isPresent()) {
            throw new AppException(ErrorCode.ADMIN_PROFILE_NOT_FOUND);
        }

        return getAdminProfileById(userId);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public AdminProfileResponse getAdminProfileById(String id) {

        AdminProfile adminProfile = adminProfileRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADMIN_PROFILE_NOT_FOUND));

        return adminMapper.toAdminProfileResponse(adminProfile);

    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public AdminProfileResponse updateMyAdminProfile() {

        var userId = SecurityContextHolder.getContext().getAuthentication().getName();

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var alreadyExists = adminProfileRepository.findById(user.getId());

        if (!alreadyExists.isPresent()) {
            throw new AppException(ErrorCode.ADMIN_PROFILE_NOT_FOUND);
        }

        AdminProfile adminProfile = alreadyExists.get();

        adminProfileRepository.save(adminProfile);

        return getAdminProfileById(userId);
}
}