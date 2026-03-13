package com.acnecare.api.common.job;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import com.acnecare.api.auth.service.InvalidatedTokenService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InvalidatedTokenCleanupJob {

    InvalidatedTokenService invalidatedTokenService;

    @Scheduled(fixedDelay = 5000)
    public void execute() {
        invalidatedTokenService.cleanInvalidatedTokens();
    }

}

// loogout: token invalidated => token cleanup job
// checktoken: introspect : invalidated jwt verify, expiryTime, exists in
// invalidatedToken table?