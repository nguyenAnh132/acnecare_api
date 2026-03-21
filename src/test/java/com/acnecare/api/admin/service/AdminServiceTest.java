package com.acnecare.api.admin.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.user.entity.User;
import com.acnecare.api.user.mapper.UserMapper;
import com.acnecare.api.user.repository.UserRepository;
import com.acnecare.api.admin.repository.AdminRepository;
import com.acnecare.api.admin.mapper.AdminMapper;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AdminRepository adminRepository;

    @Mock
    AdminMapper adminMapper;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    AdminService adminService;

    // @Test
    // void getUsers_invalidRole_shouldThrow() {
    //     AppException ex = assertThrows(AppException.class, () ->
    //             adminService.getUsers(PageRequest.of(0, 10), null, "NOT_A_ROLE", null, null, null, "createdAt", "desc"));
    //     org.junit.jupiter.api.Assertions.assertEquals(ErrorCode.INVALID_ROLE, ex.getErrorCode());
    //     verifyNoInteractions(userRepository);
    // }

    // @Test
    // void getUsers_invalidStatus_shouldThrow() {
    //     AppException ex = assertThrows(AppException.class, () ->
    //             adminService.getUsers(PageRequest.of(0, 10), null, null, "NOT_A_STATUS", null, null, "createdAt", "desc"));
    //     org.junit.jupiter.api.Assertions.assertEquals(ErrorCode.INVALID_STATUS, ex.getErrorCode());
    //     verifyNoInteractions(userRepository);
    // }

    // @Test
    // void getUsers_validFilters_shouldQueryRepository() {
    //     when(userRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(PageRequest.class)))
    //             .thenReturn(emptyPage());

    //     adminService.getUsers(PageRequest.of(0, 10), "abc", "ADMIN", "ACTIVE",
    //             LocalDate.now().minusDays(7), LocalDate.now(), "name", "asc");
    // }

    private Page<User> emptyPage() {
        return new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
    }
}
