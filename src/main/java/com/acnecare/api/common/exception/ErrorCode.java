package com.acnecare.api.common.exception;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    //success: return 1000
    USER_ALREDY_EXISTS(1001, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1003, "Role not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_PROVIDED(1004, "Role not provided", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1005, "Invalid token", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS(1006, "Invalid credentials", HttpStatus.UNAUTHORIZED),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1008, "Access denied", HttpStatus.FORBIDDEN),
    INVALID_KEY(1009, "Invalid key", HttpStatus.BAD_REQUEST),
    INVALID_FIRST_NAME(1010, "Invalid first name", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(1010, "Permission not found", HttpStatus.NOT_FOUND),
    PATIENT_PROFILE_NOT_FOUND(1011, "Patient profile not found", HttpStatus.NOT_FOUND),
    PATIENT_PROFILE_ALREADY_EXISTS(1012, "Patient profile already exists", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_ERROR(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    ;

    int code;
    String message;
    HttpStatusCode statusCode;
}