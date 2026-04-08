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

        // ==========================================
        // 1. AUTH & USER ERRORS (1000 - 1099)
        // ==========================================
        SUCCESS(1000, "Success", HttpStatus.OK),
        EMAIL_ALREADY_EXISTS(1001, "Email already exists", HttpStatus.BAD_REQUEST),
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
        INVALID_CLIENT_TYPE(1012, "Invalid client type", HttpStatus.BAD_REQUEST),
        PERMISSION_NOT_FOUND(1013, "Permission not found", HttpStatus.NOT_FOUND),
        USER_IS_BLOCKED(1014, "Your account has been blocked. Please contact Admin!", HttpStatus.FORBIDDEN),
        PASSWORD_ALREADY_SET(1015, "Password already set for this account", HttpStatus.BAD_REQUEST),
        PASSWORD_LOGIN_NOT_ENABLED(1016, "This account is using Google sign-in. Please create a password first.", HttpStatus.BAD_REQUEST),

        // ==========================================
        // 2. PATIENT PROFILE ERRORS (1100 - 1199)
        // ==========================================
        INVALID_FIRST_NAME(1100, "Invalid first name", HttpStatus.BAD_REQUEST),
        INVALID_LAST_NAME(1101, "Invalid last name", HttpStatus.BAD_REQUEST),
        INVALID_EMAIL(1102, "Invalid email", HttpStatus.BAD_REQUEST),
        INVALID_PHONE(1103, "Invalid phone", HttpStatus.BAD_REQUEST),
        INVALID_PASSWORD(1104, "Invalid password", HttpStatus.BAD_REQUEST),
        INVALID_DOB(1105, "Invalid date of birth", HttpStatus.BAD_REQUEST),
        INVALID_AVATAR_URL(1106, "Invalid avatar URL", HttpStatus.BAD_REQUEST),
        INVALID_GENDER(1107, "Invalid gender", HttpStatus.BAD_REQUEST),
        INVALID_SKIN_TYPE(1108, "Invalid skin type", HttpStatus.BAD_REQUEST),
        INVALID_ALLERGIES(1109, "Invalid allergies", HttpStatus.BAD_REQUEST),
        INVALID_HEIGHT(1110, "Invalid height", HttpStatus.BAD_REQUEST),
        INVALID_WEIGHT(1111, "Invalid weight", HttpStatus.BAD_REQUEST),
        INVALID_ADDRESS(1112, "Invalid address", HttpStatus.BAD_REQUEST),
        INVALID_REQUEST(1113, "Invalid request", HttpStatus.BAD_REQUEST),
        PATIENT_PROFILE_NOT_FOUND(1114, "Patient profile not found", HttpStatus.NOT_FOUND),
        PATIENT_PROFILE_ALREADY_EXISTS(1115, "Patient profile already exists", HttpStatus.BAD_REQUEST),
        INVALID_OLD_PASSWORD(1116, "Old password is required", HttpStatus.BAD_REQUEST),
        OLD_PASSWORD_INCORRECT(1117, "Old password is incorrect", HttpStatus.BAD_REQUEST),

        // ==========================================
        // 3. CATEGORY & PRODUCT ERRORS (2000 - 2099)
        // ==========================================
        CATEGORY_ALREADY_EXISTS(2001, "Category already exists", HttpStatus.BAD_REQUEST),
        CATEGORY_NOT_FOUND(2002, "Category not found", HttpStatus.NOT_FOUND),
        CATEGORY_IN_USE(2003, "Cannot delete category because it still contains products", HttpStatus.BAD_REQUEST),
        INVALID_CATEGORY_NAME(2004, "Invalid category name", HttpStatus.BAD_REQUEST),
        PRODUCT_NOT_FOUND(2005, "Product not found", HttpStatus.NOT_FOUND),
        PRODUCT_ALREADY_EXISTS(2006, "Product name already exists", HttpStatus.BAD_REQUEST),
        INVALID_PRODUCT_DATA(2007, "Invalid product data", HttpStatus.BAD_REQUEST),
        PRODUCT_NOT_APPROVED(2008, "Product is pending approval", HttpStatus.FORBIDDEN),
        INVALID_PRODUCT_NAME(2009, "Invalid product name", HttpStatus.BAD_REQUEST),
        INVALID_PRODUCT_BRAND(2010, "Invalid product brand", HttpStatus.BAD_REQUEST),
        INVALID_PRODUCT_DESCRIPTION(2011, "Invalid product description", HttpStatus.BAD_REQUEST),
        INVALID_PRODUCT_URL(2012, "Invalid product URL", HttpStatus.BAD_REQUEST),
        INVALID_CATEGORY_ID(2013, "Invalid category ID", HttpStatus.BAD_REQUEST),

        // ==========================================
        // 4. BRAND, ADMIN, DOCTOR & SOCIAL (3000 - 3099)
        // ==========================================
        BRAND_PROFILE_NOT_FOUND(3001, "Brand profile not found", HttpStatus.NOT_FOUND),
        BRAND_ALREADY_HAS_PROFILE(3002, "Brand already has a profile", HttpStatus.BAD_REQUEST),
        INVALID_BRAND_NAME(3003, "Invalid brand name", HttpStatus.BAD_REQUEST),
        INVALID_DESCRIPTION(3004, "Invalid description", HttpStatus.BAD_REQUEST),
        INVALID_WEBSITE(3005, "Invalid website", HttpStatus.BAD_REQUEST),
        INVALID_LOGO_URL(3006, "Invalid logo URL", HttpStatus.BAD_REQUEST),
        POST_NOT_FOUND(3007, "Post not found", HttpStatus.NOT_FOUND),
        ADMIN_PROFILE_ALREADY_EXISTS(3008, "Admin profile already exists", HttpStatus.BAD_REQUEST),
        ADMIN_PROFILE_NOT_FOUND(3009, "Admin profile not found", HttpStatus.NOT_FOUND),
        LIKE_EXISTED(3010, "You have already liked this post", HttpStatus.BAD_REQUEST),
        LIKE_NOT_FOUND(3011, "Like not found", HttpStatus.NOT_FOUND),
        DOCTOR_PROFILE_NOT_FOUND(3012, "Doctor profile not found", HttpStatus.NOT_FOUND),
        DOCTOR_ALREADY_HAS_PROFILE(3013, "Doctor already has a profile", HttpStatus.BAD_REQUEST),
        DOCTOR_PROFILE_NOT_APPROVED(3014, "Doctor profile is not approved", HttpStatus.FORBIDDEN),
        COMMENT_NOT_FOUND(3020, "Comment not found", HttpStatus.NOT_FOUND),
        BRAND_PROFILE_NOT_APPROVED(3021, "Brand profile is not approved", HttpStatus.FORBIDDEN),
        SUPPORT_ADMIN_NOT_FOUND(3022, "Support admin not found", HttpStatus.NOT_FOUND),

        // ==========================================
        // 5. APPOINTMENT & CHATROOM ERRORS (4000 - 4099)
        // ==========================================
        APPOINTMENT_NOT_FOUND(4000, "Appointment not found", HttpStatus.NOT_FOUND),
        APPOINTMENT_TIME_UNAVAILABLE(4001, "Doctor is unavailable at the selected time", HttpStatus.BAD_REQUEST),
        USER_IS_NOT_DOCTOR(4002, "Selected user is not a doctor", HttpStatus.BAD_REQUEST),
        ERROR_INVALID_MODE(4003, "Invalid appointment mode. Allowed values are ONLINE or OFFLINE.",
                        HttpStatus.BAD_REQUEST),
        ERROR_INVALID_STATUS(4004, "Invalid appointment status", HttpStatus.BAD_REQUEST),
        APPOINTMENT_CANNOT_CANCEL(4005, "Only appointments with status 'PENDING' or 'CONFIRMED' can be canceled",
                        HttpStatus.BAD_REQUEST),
        APPOINTMENT_NOT_COMPLETED(4006, "Only completed appointments can be reviewed", HttpStatus.BAD_REQUEST),
        APPOINTMENT_ALREADY_REVIEWED(4007, "This appointment has already been reviewed", HttpStatus.BAD_REQUEST),
        APPOINTMENT_CANNOT_BE_CANCELLED(4008, "This appointment cannot be cancelled", HttpStatus.BAD_REQUEST),
        CHATROOM_FORBIDDEN(4009, "Cant acceces this room", HttpStatus.FORBIDDEN),
        CHAT_ROOM_NOT_FOUND(4010, "Chat room not found", HttpStatus.NOT_FOUND),
        NO_MESSAGES_IN_ROOM(4011, "No message in room", HttpStatus.NO_CONTENT),

        // ==========================================
        // 6. CONSULTATION SERVICE & SCHEDULES (5000 - 5999)
        // ==========================================
        CONSULTATION_SERVICE_NOT_FOUND(5401, "Consultation service not found", HttpStatus.NOT_FOUND),
        INVALID_SERVICE_MODE(5402, "Invalid consultation service mode", HttpStatus.BAD_REQUEST),
        DOCTOR_SCHEDULE_NOT_FOUND(5501, "Doctor schedule not found", HttpStatus.NOT_FOUND),
        DOCTOR_SCHEDULE_TIME_CONFLICT(5502, "Doctor schedule time conflict", HttpStatus.BAD_REQUEST),
        INVALID_DOCTOR_SCHEDULE_TIME(5503, "Invalid doctor schedule time", HttpStatus.BAD_REQUEST),

        // ==========================================
        // 7. ACNE & FILE ERRORS (6000 - 7999)
        // ==========================================
        INVALID_ACNE_NAME(6001, "Invalid name", HttpStatus.BAD_REQUEST),
        INVALID_ACNE_NOTE(6002, "Invalid note", HttpStatus.BAD_REQUEST),
        ACNE_PREDICTION_NOT_FOUND(6003, "Acne prediction not found", HttpStatus.NOT_FOUND),
        FILE_EMPTY(6101, "File is empty", HttpStatus.BAD_REQUEST),
        FILE_TOO_LARGE(6102, "File size exceeds the allowed limit", HttpStatus.BAD_REQUEST),
        FILE_TYPE_NOT_ALLOWED(6103, "File type is not allowed", HttpStatus.BAD_REQUEST),
        FILE_UPLOAD_FAILED(6104, "Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR),
        FILE_DELETE_FAILED(6105, "Failed to delete file", HttpStatus.INTERNAL_SERVER_ERROR),
        INVALID_STORAGE_FOLDER(6106, "Invalid storage folder", HttpStatus.BAD_REQUEST),
        ACNE_NOT_FOUND(7001, "Acne not found", HttpStatus.NOT_FOUND),
        PATIENT_ID_REQUIRED(7002, "Patient ID is required for doctors when saving face scan results",
                        HttpStatus.BAD_REQUEST),

        // ==========================================
        // 8. TREATMENT & CONSULTATION (8000 - 8999)
        // ==========================================
        TREATMENT_CASE_NOT_FOUND(8001, "Treatment case not found", HttpStatus.NOT_FOUND),
        CONSULTATION_NOT_FOUND(8101, "Consultation not found", HttpStatus.NOT_FOUND),
        APPOINTMENT_NOT_COMPLETED_FOR_CONSULTATION(8102, "Appointment must be COMPLETED to create consultation",
                        HttpStatus.BAD_REQUEST),
        CONSULTATION_ALREADY_EXISTS_FOR_APPOINTMENT(8103, "A consultation already exists for this appointment",
                        HttpStatus.BAD_REQUEST),
        TREATMENT_PLAN_NOT_FOUND(8201, "Treatment plan not found", HttpStatus.NOT_FOUND),
        ERROR_INVALID_REQUEST(8202, "Invalid request", HttpStatus.BAD_REQUEST),
        // ==========================================
        // 9. SYSTEM ERRORS (9000+)
        // ==========================================
        UNCATEGORIZED_ERROR(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR);

        int code;
        String message;
        HttpStatusCode statusCode;
}