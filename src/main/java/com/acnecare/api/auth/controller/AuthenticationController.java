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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
            .code(1000)
            .message("Login successful")
            .result(authenticationService.authenticate(request))
            .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) 
    throws JOSEException, ParseException {

        authenticationService.logout(request);
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
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request) 
    throws JOSEException, ParseException {
        return ApiResponse.<AuthenticationResponse>builder()
            .code(1000)
            .message("Refresh successful")
            .result(authenticationService.refreshToken(request))
            .build();
    } 

}
