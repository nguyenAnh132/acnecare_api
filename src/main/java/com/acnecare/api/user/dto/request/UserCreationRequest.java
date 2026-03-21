package com.acnecare.api.user.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import java.time.LocalDate;
import java.util.Set;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Past;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 1, max = 100, message = "INVALID_FIRST_NAME")
    @NotBlank(message = "INVALID_FIRST_NAME")
    String firstName;

    @Size(min = 1, max = 100, message = "INVALID_LAST_NAME")
    @NotBlank(message = "INVALID_LAST_NAME")
    String lastName;

    @Email(message = "INVALID_EMAIL")
    String email;

    @Pattern(regexp = "^0[0-9]{9}$", message = "INVALID_PHONE")
    String phone;

    @Size(min = 8, max = 100, message = "INVALID_PASSWORD")
    @NotBlank(message = "INVALID_PASSWORD")
    String password;

    @Past(message = "INVALID_DOB")
    LocalDate dob;

    @URL(message = "INVALID_AVATAR_URL")
    String avatarUrl;

    Set<String> roles;
}
