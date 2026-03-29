package com.acnecare.api.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import com.acnecare.api.user.service.UserService;
import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.user.dto.request.UserCreationRequest;
import com.acnecare.api.user.dto.response.UserResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import com.acnecare.api.user.dto.request.UserUpdateRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("User created successfully")
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .code(1000)
                .message("Users fetched successfully")
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<UserResponse> getUserById(@PathVariable String id) {
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("User fetched successfully")
                .result(userService.getUserById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("User deleted successfully")
                .build();
    }

    @GetMapping("/me")
    ApiResponse<UserResponse> getMyInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities() != null) {
            String roles = auth.getAuthorities().stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            log.info("Roles from token: {}", roles);
        }
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("My info fetched successfully")
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/me")
    ApiResponse<UserResponse> updateMyInfo(@RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("User updated successfully")
                .result(userService.updateMyInfo(request))
                .build();
    }

    @PutMapping("/{id}/status")
    ApiResponse<UserResponse> changeUserStatus(@PathVariable String id, @RequestParam String status) {
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("User status updated successfully")
                .result(userService.changeUserStatus(id, status))
                .build();
    }

    @GetMapping("/doctors")
    public ApiResponse<List<UserResponse>> getAllDoctors() {
        return ApiResponse.<List<UserResponse>>builder()
                .code(1000)
                .message("Get all doctors successfully")
                .result(userService.getAllUsersByRole("DOCTOR"))
                .build();
    }

    @GetMapping("/doctors/active")
    public ApiResponse<List<UserResponse>> getActiveDoctors() {
        List<UserResponse> doctors = userService.getActiveUsersByRole("DOCTOR");
        String message = doctors.isEmpty() ? "No active doctors found" : "Get active doctors successfully";
        return ApiResponse.<List<UserResponse>>builder()
                .code(1000)
                .message(message)
                .result(userService.getActiveUsersByRole("DOCTOR"))
                .build();
    }

    @GetMapping("/patients")
    public ApiResponse<List<UserResponse>> getAllPatients() {
        return ApiResponse.<List<UserResponse>>builder()
                .code(1000)
                .message("Get all patients successfully")
                .result(userService.getAllUsersByRole("PATIENT"))
                .build();
    }

    @GetMapping("/patients/active")
    public ApiResponse<List<UserResponse>> getActivePatients() {
        List<UserResponse> patients = userService.getActiveUsersByRole("PATIENT");
        String message = patients.isEmpty() ? "No active patients found" : "Get active patients successfully";
        return ApiResponse.<List<UserResponse>>builder()
                .code(1000)
                .message(message)
                .result(userService.getActiveUsersByRole("PATIENT"))
                .build();
    }

    @GetMapping("/brands")
    public ApiResponse<List<UserResponse>> getAllBrands() {
        return ApiResponse.<List<UserResponse>>builder()
                .code(1000)
                .message("Get all brands successfully")
                .result(userService.getAllUsersByRole("BRAND"))
                .build();
    }

    @GetMapping("/brands/active")
    public ApiResponse<List<UserResponse>> getActiveBrands() {
        List<UserResponse> brands = userService.getActiveUsersByRole("BRAND");
        String message = brands.isEmpty() ? "No active brands found" : "Get active brands successfully";
        return ApiResponse.<List<UserResponse>>builder()
                .code(1000)
                .message(message)
                .result(userService.getActiveUsersByRole("BRAND"))
                .build();
    }
}
