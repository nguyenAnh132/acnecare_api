package com.acnecare.api.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import com.acnecare.api.auth.service.AuthenticationService;
import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.auth.dto.request.AuthenticationRequest;
import com.acnecare.api.auth.dto.response.AuthenticationResponse;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.acnecare.api.auth.dto.request.LogoutRequest;
import com.acnecare.api.auth.dto.request.IntrospectRequest;
import com.nimbusds.jose.JOSEException;
import com.acnecare.api.auth.dto.response.IntrospectResponse;
import com.acnecare.api.auth.dto.request.RefreshRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.auth.facade.AuthenticationFacade;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;
    AuthenticationFacade authenticationFacade;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request,
            HttpServletResponse httpResponse,
            HttpServletRequest httpRequest
    ) throws JOSEException, ParseException, AppException {
        return ApiResponse.<AuthenticationResponse>builder()
            .code(1000)
            .message("Login successful")
            .result(authenticationFacade.authenticate(request, httpResponse, httpRequest))
            .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request,
            HttpServletResponse httpResponse,
            HttpServletRequest httpRequest
            ) 
    throws JOSEException, ParseException, AppException {

        authenticationFacade.logout(request, httpResponse, httpRequest);
        return ApiResponse.<Void>builder()
            .code(1000)
            .message("Logout successful")
            .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) 
    throws JOSEException, ParseException {
        return ApiResponse.<IntrospectResponse>builder()
            .code(1000)
            .message("Introspect successful")
            .result(authenticationService.introspect(request))
            .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request,
            HttpServletResponse httpResponse,
            HttpServletRequest httpRequest
            ) 
    throws JOSEException, ParseException, AppException {
        return ApiResponse.<AuthenticationResponse>builder()
            .code(1000)
            .message("Refresh successful")
            .result(authenticationFacade.refreshToken(request, httpResponse, httpRequest))
            .build();
    } 

}
