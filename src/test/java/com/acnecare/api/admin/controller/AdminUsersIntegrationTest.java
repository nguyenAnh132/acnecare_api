package com.acnecare.api.admin.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.acnecare.api.role.entity.Role;
import com.acnecare.api.role.reposity.RoleReposity;
import com.acnecare.api.user.entity.User;
import com.acnecare.api.user.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AdminUsersIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleReposity roleReposity;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleReposity.deleteAll();

        Role admin = roleReposity.save(Role.builder().name("ADMIN").description("admin").build());
        Role patient = roleReposity.save(Role.builder().name("PATIENT").description("patient").build());

        userRepository.save(User.builder()
                .firstName("Alice")
                .lastName("Zed")
                .email("alice@example.com")
                .status("ACTIVE")
                .createdAt(LocalDateTime.now().minusDays(2))
                .roles(Set.of(patient))
                .build());

        userRepository.save(User.builder()
                .firstName("Bob")
                .lastName("Young")
                .email("bob@example.com")
                .status("PENDING")
                .createdAt(LocalDateTime.now().minusDays(1))
                .roles(Set.of(admin))
                .build());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getUsers_shouldSupportFilteringAndReturnMinimalFields() throws Exception {
        mockMvc.perform(get("/admins/users")
                        .param("page", "0")
                        .param("size", "10")
                        .param("role", "ADMIN")
                        .param("sortBy", "createdAt")
                        .param("sortDir", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.result.content.length()").value(1))
                .andExpect(jsonPath("$.result.content[0].email").value("bob@example.com"))
                .andExpect(jsonPath("$.result.content[0].username").value("bob@example.com"))
                .andExpect(jsonPath("$.result.content[0].role").value("ADMIN"))
                .andExpect(jsonPath("$.result.content[0].status").value("PENDING"))
                .andExpect(jsonPath("$.result.content[0].createdAt").exists())
                // should not expose other fields
                .andExpect(jsonPath("$.result.content[0].fullName").doesNotExist())
                .andExpect(jsonPath("$.result.content[0].phone").doesNotExist())
                .andExpect(jsonPath("$.result.content[0].dob").doesNotExist())
                .andExpect(jsonPath("$.result.content[0].roles").doesNotExist());
    }

    @Test
    @WithMockUser(authorities = "ROLE_PATIENT")
    void getUsers_nonAdmin_shouldBeForbiddenByMethodSecurity() throws Exception {
        // Note: filters are disabled; this test still validates method-level security via @PreAuthorize
        mockMvc.perform(get("/admins/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isForbidden());
    }
}

