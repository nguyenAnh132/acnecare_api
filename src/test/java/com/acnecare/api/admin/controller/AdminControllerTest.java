package com.acnecare.api.admin.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.acnecare.api.admin.dto.response.AdminProfileResponse;
import com.acnecare.api.admin.service.AdminService;
import org.mockito.Mock;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    private AdminProfileResponse adminProfileResponse;

    // @Test
    // @WithMockUser(authorities = "ROLE_ADMIN")
    // void getUsers_Success() throws Exception {
    //     Page<UserAdminResponse> userPage = new PageImpl<>(List.of(userAdminResponse));

    //     when(adminService.getUsers(any(Pageable.class), any(), any(), any(), any(), any(), any(), any())).thenReturn(userPage);

    //     mockMvc.perform(get("/admins/users")
    //             .param("page", "0")
    //             .param("size", "10")
    //             .contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.code").value(1000))
    //             .andExpect(jsonPath("$.result.content[0].email").value("test@example.com"));
    // }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getUsers_Forbidden() throws Exception {
        mockMvc.perform(get("/admins/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
