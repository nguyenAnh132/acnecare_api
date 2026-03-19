package com.acnecare.api.admin.service;

import java.util.Locale;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

import com.acnecare.api.admin.entity.AdminProfile;

import com.acnecare.api.user.entity.User;
import com.acnecare.api.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import com.acnecare.api.admin.mapper.AdminProfileMapper;
import com.acnecare.api.admin.dto.response.AdminProfileResponse;

import java.time.LocalDateTime;

import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;

import org.springframework.security.core.context.SecurityContextHolder;

import com.acnecare.api.common.helper.CurrentUserId;

import org.springframework.security.access.prepost.PreAuthorize;

import com.acnecare.api.admin.repository.AdminProfileRepository;

import com.acnecare.api.admin.dto.request.AdminProfileUpdateRequest;
import com.acnecare.api.admin.dto.response.UserAdminResponse;
import com.acnecare.api.user.mapper.UserMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminService {

    UserRepository userRepository;
    AdminProfileRepository adminProfileRepository;
    AdminProfileMapper adminProfileMapper;
    UserMapper userMapper;

    @Cacheable(cacheNames = "users", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #search + '-' + #role + '-' + #status + '-' + #startDate + '-' + #endDate + '-' + #sortBy + '-' + #sortDir")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<UserAdminResponse> getUsers(
            Pageable pageable, String search, String role, String status,
            LocalDate startDate, LocalDate endDate,
            String sortBy, String sortDir) {

        String normalizedRole = normalizeRole(role);
        String normalizedStatus = normalizeStatus(status);
        Sort sort = buildSort(sortBy, sortDir);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        log.info("Admin fetching user list with params: search={}, role={}, status={}, startDate={}, endDate={}, sortBy={}, sortDir={}",
                search, normalizedRole, normalizedStatus, startDate, endDate, sortBy, sortDir);

        Specification<User> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isBlank()) {
                String pattern = "%" + search.toLowerCase() + "%";
                Predicate searchPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), pattern)
                );
                predicates.add(searchPredicate);
            }

            if (normalizedRole != null) {
                predicates.add(criteriaBuilder.equal(root.join("roles").get("name"), normalizedRole));
            }

            if (normalizedStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), normalizedStatus));
            }

            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate.atStartOfDay()));
            }

            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate.plusDays(1).atStartOfDay()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        try {
            Page<User> userPage = userRepository.findAll(spec, sortedPageable);
            log.info("Found {} users matching criteria", userPage.getTotalElements());
            return userPage.map(userMapper::toUserAdminResponse);
        } catch (Exception e) {
            log.error("Error fetching user list: {}", e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_ERROR);
        }
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) return null;
        String value = role.trim().toUpperCase(Locale.ROOT);
        try {
            com.acnecare.api.user.enums.UserRole.valueOf(value);
            return value;
        } catch (IllegalArgumentException ex) {
            throw new AppException(ErrorCode.INVALID_ROLE);
        }
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) return null;
        String value = status.trim().toUpperCase(Locale.ROOT);
        try {
            com.acnecare.api.user.enums.UserStatus.valueOf(value);
            return value;
        } catch (IllegalArgumentException ex) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }
    }

    private Sort buildSort(String sortBy, String sortDir) {
        String by = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy.trim();
        String dir = (sortDir == null || sortDir.isBlank()) ? "desc" : sortDir.trim();

        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(dir);
        } catch (IllegalArgumentException ex) {
            direction = Sort.Direction.DESC;
        }

        return switch (by) {
            case "name" -> Sort.by(direction, "firstName").and(Sort.by(direction, "lastName"));
            case "createdAt" -> Sort.by(direction, "createdAt");
            case "username", "email" -> Sort.by(direction, "email");
            default -> Sort.by(direction, "createdAt");
        };
    }

    public void createMyAdminProfile(User user) {

        var userId = CurrentUserId.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var isAlreadyExists = adminProfileRepository.existsByUserId(userId);

        if (isAlreadyExists) {
            throw new AppException(ErrorCode.ADMIN_PROFILE_ALREADY_EXISTS);
        }

        AdminProfile adminProfile = new AdminProfile();

        adminProfile.setUser(user);
        adminProfile.setCreatedAt(LocalDateTime.now());
        adminProfile.setUpdatedAt(LocalDateTime.now());
       

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

        return adminProfileMapper.toAdminProfileResponse(adminProfile);

    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public AdminProfileResponse updateMyAdminProfile(AdminProfileUpdateRequest request) {
        var userId = CurrentUserId.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        var adminProfile = adminProfileRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.ADMIN_PROFILE_NOT_FOUND));
        adminProfileMapper.updateAdminProfile(request, adminProfile);
        adminProfile.setUpdatedAt(LocalDateTime.now());
        return adminProfileMapper.toAdminProfileResponse(adminProfileRepository.save(adminProfile));
    }
}