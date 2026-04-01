package com.acnecare.api.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; // 🚨 IMPORT
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

import com.acnecare.api.common.storage.FileStorageService; // 🚨 IMPORT
import com.acnecare.api.common.storage.StorageFolder; // 🚨 IMPORT

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
    FileStorageService fileStorageService; // 🚨 KHAI BÁO THÊM SERVICE LƯU ẢNH

    // #region PUBLIC METHODS
    public UserResponse createUser(UserCreationRequest request, MultipartFile avatar) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);

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

        // BƯỚC 1: Lưu User trước để hệ thống sinh ra chuỗi ID (UUID)
        user = userRepository.save(user);

        // BƯỚC 2: Nếu có file ảnh được gửi lên, lưu ảnh và cập nhật URL
        if (avatar != null && !avatar.isEmpty()) {
            var fileRes = fileStorageService.store(avatar, StorageFolder.avatar, user.getId());
            user.setAvatarUrl(fileRes.getUrl());
            userRepository.save(user); // Lưu lại lần nữa để cập nhật URL
        }

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

        if ("ACTIVE".equals(status)) {
            boolean isDoctor = user.getRoles().stream()
                    .anyMatch(role -> "DOCTOR".equals(role.getName()));
            boolean isBrand = user.getRoles().stream()
                    .anyMatch(role -> "BRAND".equals(role.getName()));

            if (isDoctor) {
                try {
                    var doctorProfile = doctorService.getDoctorProfileById(id);

                    if (!"ACCEPTED".equals(doctorProfile.getVerificationStatus())) {
                        throw new AppException(ErrorCode.DOCTOR_PROFILE_NOT_APPROVED);
                    }
                } catch (AppException e) {
                    if (e.getErrorCode() == ErrorCode.DOCTOR_PROFILE_NOT_FOUND) {
                        throw new AppException(ErrorCode.DOCTOR_PROFILE_NOT_APPROVED);
                    }
                    throw e;
                }
            }
            if (isBrand) {
                try {
                    var brandProfile = brandService.getBrandProfileById(id);
                    String brandStatus = brandProfile.getVerificationStatus();
                    if (!"ACCEPTED".equals(brandStatus) && !"APPROVED".equals(brandStatus)) {
                        throw new AppException(ErrorCode.BRAND_PROFILE_NOT_APPROVED);
                    }
                } catch (AppException e) {
                    throw new AppException(ErrorCode.BRAND_PROFILE_NOT_APPROVED);
                }
            }
        }

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

    public UserResponse updateMyInfo(UserUpdateRequest request, MultipartFile avatar) {
        User user = getMe();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setDob(request.getDob());
        if (request.getAvatarUrl() != null && !request.getAvatarUrl().isBlank()) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        user.setUpdatedAt(LocalDateTime.now());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            user.setRoles(getRolesFromRequest(request.getRoles()));
        }

        // Xử lý upload và ghi đè ảnh mới
        if (avatar != null && !avatar.isEmpty()) {
            // Xóa ảnh cũ (nếu có) để tránh rác ổ cứng
            if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                try {
                    fileStorageService.delete(user.getAvatarUrl());
                } catch (Exception e) {
                    log.warn("Không thể xóa ảnh avatar cũ của user {}: {}", user.getId(), e.getMessage());
                }
            }

            // Upload ảnh mới
            var fileRes = fileStorageService.store(avatar, StorageFolder.avatar, user.getId());
            user.setAvatarUrl(fileRes.getUrl());
        }

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