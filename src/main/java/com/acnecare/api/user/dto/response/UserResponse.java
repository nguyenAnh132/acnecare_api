package com.acnecare.api.user.dto.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import com.acnecare.api.role.entity.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String firstName;
    String lastName;
    String email;
    String phone;
    LocalDate dob;
    String avatarUrl;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime lastLoginAt;

    Set<Role> roles;
}
