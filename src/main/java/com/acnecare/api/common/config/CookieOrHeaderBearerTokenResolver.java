package com.acnecare.api.common.config;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import com.acnecare.api.auth.service.AuthCookieService;


@Component
@RequiredArgsConstructor
public class CookieOrHeaderBearerTokenResolver implements BearerTokenResolver {


    private final AuthCookieService authCookieService;
    private static final DefaultBearerTokenResolver DEFAULT_BEARER_TOKEN_RESOLVER = new DefaultBearerTokenResolver();

    @Override
    public String resolve(HttpServletRequest request) {
        String headerToken = DEFAULT_BEARER_TOKEN_RESOLVER.resolve(request);

        if (headerToken != null && !headerToken.isBlank()) {
            return headerToken;
        }

        String cookieToken = authCookieService.readAccessToken(request);
        if (cookieToken != null && !cookieToken.isBlank()) {
            return cookieToken;
        }

        return null;
    }

}
