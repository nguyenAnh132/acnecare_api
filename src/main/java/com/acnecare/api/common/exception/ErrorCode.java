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
    PERMISSION_NOT_FOUND(1010, "Permission not found", HttpStatus.NOT_FOUND),
    USER_IS_BLOCKED(1019, "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ Admin!", HttpStatus.FORBIDDEN),

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
    INVALID_EMAIL_BRAND(3007, "Invalid email", HttpStatus.BAD_REQUEST),
    INVALID_PHONE_BRAND(3008, "Invalid phone", HttpStatus.BAD_REQUEST),

    // Doctor profile error codes
    DOCTOR_PROFILE_NOT_FOUND(3011, "Doctor profile not found", HttpStatus.NOT_FOUND),
    DOCTOR_ALREADY_HAS_PROFILE(3012, "Doctor already has a profile", HttpStatus.BAD_REQUEST),

    // VALIDATION ERRORS
    // patient profile
    INVALID_FIRST_NAME(1010, "Invalid first name", HttpStatus.BAD_REQUEST),
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
    // product
<<<<<<< Updated upstream
    PRODUCT_NOT_FOUND(2004, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTS(2005, "Product name already exists", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_DATA(2006, "Invalid product data", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_APPROVED(2007, "Product is pending approval", HttpStatus.FORBIDDEN), 
    // admin profile
    ADMIN_PROFILE_ALREADY_EXISTS(3008, "Admin profile already exists", HttpStatus.BAD_REQUEST),
    ADMIN_PROFILE_NOT_FOUND(3009, "Admin profile not found", HttpStatus.NOT_FOUND)
    
=======
    PRODUCT_NOT_FOUND(2005, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTS(2006, "Product name already exists", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_DATA(2007, "Invalid product data", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_APPROVED(2008, "Product is pending approval", HttpStatus.FORBIDDEN),
    INVALID_PRODUCT_NAME(2009, "Tên sản phẩm không hợp lệ (bắt buộc nhập)", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_BRAND(2010, "Thương hiệu không được để trống", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_DESCRIPTION(2011, "Mô tả sản phẩm không được để trống", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_URL(2012, "Đường dẫn (URL) không đúng định dạng", HttpStatus.BAD_REQUEST),
    INVALID_CATEGORY_ID(2013, "Danh mục sản phẩm (Category ID) không được để trống", HttpStatus.BAD_REQUEST),

>>>>>>> Stashed changes
    ;

    int code;
    String message;
    HttpStatusCode statusCode;
}