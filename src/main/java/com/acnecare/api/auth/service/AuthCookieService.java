package com.acnecare.api.auth.service;  

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import java.time.Duration;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthCookieService {

    @Value("${auth.cookie.access-token-name}")
    @NonFinal
    String accessTokenName;

    @Value("${auth.cookie.refresh-token-name}")
    @NonFinal
    String refreshTokenName;

    @Value("${auth.cookie.http-only}")
    @NonFinal
    boolean httpOnly;

    @Value("${auth.cookie.secure}")
    @NonFinal
    boolean secure;

    @Value("${auth.cookie.same-site}")
    @NonFinal
    String sameSite;

    @Value("${auth.cookie.max-access-token-age}")
    @NonFinal
    int maxAccessTokenAge;

    @Value("${auth.cookie.max-refresh-token-age}")
    @NonFinal
    int maxRefreshTokenAge;

    @Value("${auth.cookie.access-path}")
    @NonFinal
    String accessPath;

    @Value("${auth.cookie.refresh-path}")
    @NonFinal
    String refreshPath;

    public void setAccessToken(HttpServletResponse response, String accessToken) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(accessTokenName, accessToken)
                .httpOnly(httpOnly)
                .secure(secure)
                .sameSite(sameSite)
                .path(accessPath)
                .maxAge(Duration.ofSeconds(maxAccessTokenAge));
        response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());
    }

    public void setRefreshToken(HttpServletResponse response, String refreshToken){

        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(refreshTokenName, refreshToken)
            .httpOnly(httpOnly)
            .secure(secure)
            .sameSite(sameSite)
            .path(refreshPath)
            .maxAge(Duration.ofSeconds(maxRefreshTokenAge));
            response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());

    }

    public String readAccessToken(HttpServletRequest request){
        return readCookie(request, accessTokenName);
    }

    public String readRefreshToken(HttpServletRequest request){
        return readCookie(request, refreshTokenName);
    }

    public void deleteAccessToken(HttpServletResponse response){
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(accessTokenName, "")
        .httpOnly(httpOnly)
        .secure(secure)
        .sameSite(sameSite)
        .path(accessPath)
        .maxAge(Duration.ofSeconds(0));
        response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());
    }

    public void deleteRefreshToken(HttpServletResponse response){
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(refreshTokenName, "")
        .httpOnly(httpOnly)
        .secure(secure)
        .sameSite(sameSite)
        .path(refreshPath)
        .maxAge(Duration.ofSeconds(0));
        response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());
    }

    private String readCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
