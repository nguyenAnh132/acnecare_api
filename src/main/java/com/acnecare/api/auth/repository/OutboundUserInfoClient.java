package com.acnecare.api.auth.repository;

import com.acnecare.api.auth.dto.response.GoogleUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "outbound-user-info", url = "${outbound.identity.user-info-uri:https://www.googleapis.com}")
public interface OutboundUserInfoClient {
    @GetMapping("/oauth2/v3/userinfo")
    GoogleUserInfoResponse getUserInfo(@RequestHeader("Authorization") String authorization);
}
