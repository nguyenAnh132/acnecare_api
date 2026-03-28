package com.acnecare.api.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import com.acnecare.api.user.repository.UserRepository;
import com.acnecare.api.user.mapper.UserMapper;
import java.time.LocalDateTime;
import com.acnecare.api.user.entity.User;
import com.acnecare.api.user.enums.UserStatus;
import com.acnecare.api.user.dto.request.UserCreationRequest;
import com.acnecare.api.user.dto.response.UserResponse;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.role.entity.Role;
import com.acnecare.api.role.reposity.RoleReposity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import com.acnecare.api.user.dto.request.UserUpdateRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import com.acnecare.api.patient.service.PatientProfileService;
import com.acnecare.api.doctor.service.DoctorService;
import com.acnecare.api.brand.service.BrandService;
import com.acnecare.api.admin.service.AdminService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleReposity roleRepository;
    PatientProfileService patientService;
    DoctorService doctorService;
    BrandService brandService;
    AdminService adminService;

    // #region PUBLIC METHODS
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_ALREDY_EXISTS);

        User user = userMapper.toUser(request);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setLastLoginAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var role = getRolesFromRequest(request.getRoles());
        user.setRoles(role);
        if (request.getRoles().contains("PATIENT") || request.getRoles().contains("ADMIN")) {
            user.setStatus(UserStatus.ACTIVE.name());
        } else {
            user.setStatus(UserStatus.PENDING.name());
        }
        userRepository.save(user);

        if (request.getRoles().contains("PATIENT")) {
            patientService.createMyPatientProfile(user);
        } else if (request.getRoles().contains("ADMIN")) {
            adminService.createMyAdminProfile(user);
        } else if (request.getRoles().contains("DOCTOR")) {
            doctorService.createMyDoctorProfile(user);
        } else if (request.getRoles().contains("BRAND")) {
            brandService.createMyBrandProfile(user);
        }

        return userMapper.toUserCreationResponse(user);
    }
    // #endregion

    private Set<Role> getRolesFromRequest(Set<String> roles) {
        if (roles != null && !roles.isEmpty()) {
            var validRoles = roleRepository.findAllById(roles);
            if (roles.size() != validRoles.size())
                throw new AppException(ErrorCode.ROLE_NOT_FOUND);
            return new HashSet<>(validRoles);
        } else {
            throw new AppException(ErrorCode.ROLE_NOT_PROVIDED);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional(readOnly = true)
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserCreationResponse(user);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userMapper.toUserCreationResponses(userRepository.findAll());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserResponse changeUserStatus(String id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());

        return userMapper.toUserCreationResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse getMyInfo() {
        return userMapper.toUserCreationResponse(getMe());
    }

    @Transactional(readOnly = true)
    private User getMe() {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public UserResponse updateMyInfo(UserUpdateRequest request) {
        User user = getMe();

        userMapper.updateUser(request, user);
        user.setUpdatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getRolesFromRequest(request.getRoles()));

        return userMapper.toUserCreationResponse(userRepository.save(user));
    }

    public List<UserResponse> getAllUsersByRole(String roleName) {
        List<User> users = userRepository.findByRoles_Name(roleName);
        return userMapper.toUserResponseList(users);
    }

    public List<UserResponse> getActiveUsersByRole(String roleName) {
        List<User> users = userRepository.findByRoles_Name(roleName);

        List<User> activeUsers = users.stream()
                .filter(user -> "ACTIVE".equals(user.getStatus()))
                .toList();

        return userMapper.toUserResponseList(activeUsers);
    }
}
