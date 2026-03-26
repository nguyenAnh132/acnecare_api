package com.acnecare.api.common.exception;

import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public enum ErrorCode {
    // success: return 1000
    EMAIL_ALREDY_EXISTS(1001, "Email already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1003, "Role not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_PROVIDED(1004, "Role not provided", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1005, "Invalid token", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS(1006, "Invalid credentials", HttpStatus.UNAUTHORIZED),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1008, "Access denied", HttpStatus.FORBIDDEN),
    INVALID_KEY(1009, "Invalid key", HttpStatus.BAD_REQUEST),
    INVALID_ROLE(1010, "Invalid role", HttpStatus.BAD_REQUEST),
    INVALID_STATUS(1011, "Invalid status", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(1010, "Permission not found", HttpStatus.NOT_FOUND),
    USER_IS_BLOCKED(1019, "Your account has been blocked. Please contact Admin!", HttpStatus.FORBIDDEN),

    PATIENT_PROFILE_NOT_FOUND(1011, "Patient profile not found", HttpStatus.NOT_FOUND),
    PATIENT_PROFILE_ALREADY_EXISTS(1012, "Patient profile already exists", HttpStatus.BAD_REQUEST),

    UNCATEGORIZED_ERROR(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    // Brand profile error codes
    BRAND_PROFILE_NOT_FOUND(3001, "Brand profile not found", HttpStatus.NOT_FOUND),
    BRAND_ALREADY_HAS_PROFILE(3002, "Brand already has a profile", HttpStatus.BAD_REQUEST),
    INVALID_BRAND_NAME(3003, "Invalid brand name", HttpStatus.BAD_REQUEST),
    INVALID_DESCRIPTION(3004, "Invalid description", HttpStatus.BAD_REQUEST),
    INVALID_WEBSITE(3005, "Invalid website", HttpStatus.BAD_REQUEST),
    INVALID_LOGO_URL(3006, "Invalid logo URL", HttpStatus.BAD_REQUEST),
    
    //Post
    POST_NOT_FOUND(3007, "Post not found", HttpStatus.NOT_FOUND),
    //Like
    LIKE_EXISTED(3010, "You have already liked this post", HttpStatus.BAD_REQUEST),
    LIKE_NOT_FOUND(3011, "Like not found", HttpStatus.NOT_FOUND),
    // Comment
    COMMENT_NOT_FOUND(3020,"Comment not found", HttpStatus.NOT_FOUND),
    // Doctor profile error codes
    DOCTOR_PROFILE_NOT_FOUND(3011, "Doctor profile not found", HttpStatus.NOT_FOUND),
    DOCTOR_ALREADY_HAS_PROFILE(3012, "Doctor already has a profile", HttpStatus.BAD_REQUEST),

    // Acne prediction error codes
    ACNE_PREDICTION_NOT_FOUND(6003, "Acne prediction not found", HttpStatus.NOT_FOUND),

    // Acne error codes
    ACNE_NOT_FOUND(7001, "Acne not found", HttpStatus.NOT_FOUND),

    // VALIDATION ERRORS
    // acne prediction
    INVALID_NAME(6001, "Invalid name", HttpStatus.BAD_REQUEST),
    INVALID_NOTE(6002, "Invalid note", HttpStatus.BAD_REQUEST),

    // acne
    PATIENT_ID_REQUIRED(7001, "Patient ID is required for doctors when saving face scan results",
            HttpStatus.BAD_REQUEST),

    // patient profile
    INVALID_FIRST_NAME(1100, "Invalid first name", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1013, "Invalid email", HttpStatus.BAD_REQUEST),
    INVALID_LAST_NAME(1014, "Invalid last name", HttpStatus.BAD_REQUEST),
    INVALID_PHONE(1015, "Invalid phone", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1016, "Invalid password", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1017, "Invalid date of birth", HttpStatus.BAD_REQUEST),
    INVALID_AVATAR_URL(1018, "Invalid avatar URL", HttpStatus.BAD_REQUEST),
    INVALID_GENDER(1019, "Invalid gender", HttpStatus.BAD_REQUEST),
    INVALID_SKIN_TYPE(1020, "Invalid skin type", HttpStatus.BAD_REQUEST),
    INVALID_ALLERGIES(1021, "Invalid allergies", HttpStatus.BAD_REQUEST),
    INVALID_HEIGHT(1022, "Invalid height", HttpStatus.BAD_REQUEST),
    INVALID_WEIGHT(1023, "Invalid weight", HttpStatus.BAD_REQUEST),
    INVALID_ADDRESS(1024, "Invalid address", HttpStatus.BAD_REQUEST),
    // DOMAIN ERRORS
    // category
    CATEGORY_ALREADY_EXISTS(2001, "Category already exists", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(2002, "Category not found", HttpStatus.NOT_FOUND),
    CATEGORY_IN_USE(2003, "Cannot delete category because it still contains products", HttpStatus.BAD_REQUEST),
    INVALID_CATEGORY_NAME(2004, "Invalid category name", HttpStatus.BAD_REQUEST),

    // admin profile
    ADMIN_PROFILE_ALREADY_EXISTS(3008, "Admin profile already exists", HttpStatus.BAD_REQUEST),
    ADMIN_PROFILE_NOT_FOUND(3009, "Admin profile not found", HttpStatus.NOT_FOUND),

    PRODUCT_NOT_FOUND(2005, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTS(2006, "Product name already exists", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_DATA(2007, "Invalid product data", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_APPROVED(2008, "Product is pending approval", HttpStatus.FORBIDDEN),

    INVALID_PRODUCT_NAME(2009, "Invalid product name", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_BRAND(2010, "Invalid product brand", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_DESCRIPTION(2011, "Invalid product description", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_URL(2012, "Invalid product URL", HttpStatus.BAD_REQUEST),
    INVALID_CATEGORY_ID(2013, "Invalid category ID", HttpStatus.BAD_REQUEST),

    // APPOINTMENT ERRORS
    APPOINTMENT_NOT_FOUND(4000, "Appointment not found", HttpStatus.NOT_FOUND),
    APPOINTMENT_TIME_UNAVAILABLE(4001, "Doctor is unavailable at the selected time", HttpStatus.BAD_REQUEST),
    USER_IS_NOT_DOCTOR(4002, "Selected user is not a doctor", HttpStatus.BAD_REQUEST),
    ERROR_INVALID_MODE(4003, "Invalid appointment mode. Allowed values are ONLINE or OFFLINE.", HttpStatus.BAD_REQUEST),
    ERROR_INVALID_STATUS(4004, "Invalid appointment status", HttpStatus.BAD_REQUEST),
    APPOINTMENT_CANNOT_CANCEL(4005, "Only appointments with status 'PENDING' or 'CONFIRMED' can be canceled",
            HttpStatus.BAD_REQUEST),
    APPOINTMENT_NOT_COMPLETED(4006, "Only completed appointments can be reviewed", HttpStatus.BAD_REQUEST),
    APPOINTMENT_ALREADY_REVIEWED(4007, "This appointment has already been reviewed", HttpStatus.BAD_REQUEST),
    APPOINTMENT_CANNOT_BE_CANCELLED(4008, "This appointment cannot be cancelled", HttpStatus.BAD_REQUEST),

    CONSULTATION_SERVICE_NOT_FOUND(5401, "Consultation service not found", HttpStatus.NOT_FOUND),
    INVALID_SERVICE_MODE(5402, "Invalid consultation service mode", HttpStatus.BAD_REQUEST),
    DOCTOR_SCHEDULE_NOT_FOUND(5501, "Doctor schedule not found", HttpStatus.NOT_FOUND),
    DOCTOR_SCHEDULE_TIME_CONFLICT(5502, "Doctor schedule time conflict", HttpStatus.BAD_REQUEST),
    INVALID_DOCTOR_SCHEDULE_TIME(5503, "Invalid doctor schedule time", HttpStatus.BAD_REQUEST),
    
    
    // TREATMENT CASE ERRORS
    TREATMENT_CASE_NOT_FOUND(8001, "Treatment case not found", HttpStatus.NOT_FOUND),

    // CONSULTATION ERRORS
    CONSULTATION_NOT_FOUND(8101, "Consultation not found", HttpStatus.NOT_FOUND),
    APPOINTMENT_NOT_COMPLETED_FOR_CONSULTATION(8102, "Appointment must be COMPLETED to create consultation", HttpStatus.BAD_REQUEST),
    CONSULTATION_ALREADY_EXISTS_FOR_APPOINTMENT(8103, "A consultation already exists for this appointment", HttpStatus.BAD_REQUEST),

     // FILE UPLOAD ERRORS
    FILE_EMPTY(6001, "File is empty", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(6002, "File size exceeds the allowed limit", HttpStatus.BAD_REQUEST),
    FILE_TYPE_NOT_ALLOWED(6003, "File type is not allowed", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(6004, "Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_FAILED(6005, "Failed to delete file", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_STORAGE_FOLDER(6006, "Invalid storage folder", HttpStatus.BAD_REQUEST),
    ;

    int code;
    String message;
    HttpStatusCode statusCode;
}