package com.acnecare.api.user.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    //validate first name
    @Size(min = 5, max = 100, message = "INVALID_FIRST_NAME")
    String firstName;

    String lastName;
    String email;
    String phone;
    String password;
    LocalDate dob;
    String avatarUrl;
    Set<String> roles;
}
