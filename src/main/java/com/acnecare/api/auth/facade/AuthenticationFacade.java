package com.acnecare.api.auth.facade;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import com.acnecare.api.auth.service.AuthenticationService;
import com.acnecare.api.auth.service.AuthCookieService;
import com.acnecare.api.auth.dto.request.AuthenticationRequest;
import com.acnecare.api.auth.dto.response.AuthenticationResponse;
import com.acnecare.api.common.exception.AppException;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import com.acnecare.api.auth.dto.request.RefreshRequest;
import com.acnecare.api.auth.dto.request.LogoutRequest;
import com.acnecare.api.common.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFacade {

    AuthenticationService authenticationService;
    AuthCookieService authCookieService;

    public AuthenticationResponse authenticate(AuthenticationRequest body,
            HttpServletResponse httpResponse,
            HttpServletRequest httpRequest
            )
            throws JOSEException, ParseException, AppException {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(body);

        String clientType = getClientType(httpRequest);
        if(!clientType.equals(ClientType.MOBILE.name())) {
            authCookieService.setAccessToken(httpResponse, authenticationResponse.getAccessToken());
            authCookieService.setRefreshToken(httpResponse, authenticationResponse.getRefreshToken());
        }

        return authenticationResponse;
    }

    public AuthenticationResponse refreshToken(RefreshRequest body,
            HttpServletResponse httpResponse,
            HttpServletRequest httpRequest
            )
            throws JOSEException, ParseException, AppException {
        String refreshToken = firstNonBlank(body.getRefreshToken(), authCookieService.readRefreshToken(httpRequest));

        if (refreshToken == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(
                RefreshRequest.builder().refreshToken(refreshToken).build());

        String clientType = getClientType(httpRequest);
        if(!clientType.equals(ClientType.MOBILE.name())) {
            authCookieService.setAccessToken(httpResponse, authenticationResponse.getAccessToken());
            authCookieService.setRefreshToken(httpResponse, authenticationResponse.getRefreshToken());
        }
        return authenticationResponse;
    }

    public void logout(LogoutRequest body,
            HttpServletResponse httpResponse,
            HttpServletRequest httpRequest
            )
            throws JOSEException, ParseException, AppException {
        String accessToken = firstNonBlank(body.getAccessToken(), authCookieService.readAccessToken(httpRequest));
        String refreshToken = firstNonBlank(body.getRefreshToken(), authCookieService.readRefreshToken(httpRequest));

        if (accessToken == null || refreshToken == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        authenticationService.logout(LogoutRequest.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());

        String clientType = getClientType(httpRequest);
        if(!clientType.equals(ClientType.MOBILE.name())) {
            authCookieService.deleteAccessToken(httpResponse);
            authCookieService.deleteRefreshToken(httpResponse);
        }
    }

    public enum ClientType {WEB, MOBILE}

    private String firstNonBlank(String primary, String secondary) {
        if (primary != null && !primary.isBlank()) {
            return primary;
        }
        if (secondary != null && !secondary.isBlank()) {
            return secondary;
        }
        return null;
    }

    private String getClientType(HttpServletRequest httpRequest) {
        String header = httpRequest.getHeader("X-Client-Type");
        if (header == null || header.isBlank()) {
            return ClientType.MOBILE.name();
        }

        try {
            return ClientType.valueOf(header.toUpperCase()).name();
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_CLIENT_TYPE);
        }
    }

}
