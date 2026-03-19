package com.acnecare.api.auth.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import java.util.Date;
import com.acnecare.api.auth.repository.InvalidatedRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InvalidatedTokenService {

    InvalidatedRepository invalidatedRepository;

    public void cleanInvalidatedTokens() {
        var now = new Date();
        var invalidatedTokens = invalidatedRepository
                .findAllByExpiryTimeBefore(now);

        if (!invalidatedTokens.isEmpty()) {
            invalidatedRepository.deleteAll(invalidatedTokens);
        }
    }

}
