package com.acnecare.api.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.acnecare.api.role.reposity.RoleReposity;
import com.acnecare.api.user.repository.UserRepository;
import com.acnecare.api.user.entity.User;
import org.springframework.boot.ApplicationRunner;
import com.acnecare.api.role.entity.Role;
import java.time.LocalDateTime;
import java.util.Set;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.acne.repository.AcneRepository;
import com.acnecare.api.acne.entity.Acne;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleReposity roleRepository,
            AcneRepository acneRepository) {
        return args -> {
            // if (acneRepository.count() == 0) {
            // List<Acne> acnes = List.of(
            // Acne.builder()
            // .name("Mụn đầu đen")
            // .description("Acne description")
            // .createdAt(LocalDateTime.now())
            // .build(),
            // Acne.builder()
            // .name("Mụn ẩn")
            // .description("Acne description")
            // .createdAt(LocalDateTime.now())
            // .build()
            // );
            // acneRepository.saveAll(acnes);
            // }
            if (!userRepository.existsByEmail("admin@gmail.com")
                    && !roleRepository.existsByName("ADMIN")
                    && !roleRepository.existsByName("PATIENT")
                    && !roleRepository.existsByName("DOCTOR")
                    && !roleRepository.existsByName("BRAND")) {

                roleRepository.save(Role.builder()
                        .name("ADMIN")
                        .description("Admin role")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());

                roleRepository.save(Role.builder()
                        .name("PATIENT")
                        .description("Patient role")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());

                roleRepository.save(Role.builder()
                        .name("DOCTOR")
                        .description("Doctor role")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());

                roleRepository.save(Role.builder()
                        .name("BRAND")
                        .description("Brand role")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());

                userRepository.save(User.builder()
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("12345678"))
                        .roles(Set.of(roleRepository.findById("ADMIN")
                                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND))))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .lastLoginAt(LocalDateTime.now())
                        .status("ACTIVE")
                        .lastName("Admin")
                        .build());

            }

        };
    }

}
