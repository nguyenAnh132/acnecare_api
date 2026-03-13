package com.acnecare.api.auth.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.AccessLevel;
import com.acnecare.api.user.repository.UserRepository;
import com.acnecare.api.auth.repository.InvalidatedRepository;

import java.time.Instant;
import java.util.Date;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import com.nimbusds.jose.JOSEException;

import com.acnecare.api.user.entity.User;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jose.JWSAlgorithm;
import java.util.UUID;
import java.util.StringJoiner;
import org.springframework.util.CollectionUtils;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.acnecare.api.auth.dto.response.IntrospectResponse;
import com.acnecare.api.auth.entity.InvalidatedToken;
import com.acnecare.api.auth.dto.request.IntrospectRequest;
import java.text.ParseException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.acnecare.api.auth.dto.request.AuthenticationRequest;
import com.acnecare.api.auth.dto.response.AuthenticationResponse;
import com.acnecare.api.auth.dto.request.LogoutRequest;
import com.acnecare.api.auth.dto.request.RefreshRequest;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    @NonFinal
    @Value("${jwt.signer_key}")
    protected String signerKey;

    @NonFinal
    @Value("${jwt.access-token-valid-duration}")
    protected long accessTokenValidDuration;

    @NonFinal
    @Value("${jwt.refresh-token-valid-duration}")
    protected long refreshTokenValidDuration;

    UserRepository userRepository;
    InvalidatedRepository invalidatedRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if ("BLOCK".equals(user.getStatus())) {
            throw new AppException(ErrorCode.USER_IS_BLOCKED);
        }
        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isAuthenticated) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .isAuthenticated(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshRequest request)
            throws JOSEException, ParseException {

        var signedJWT = verifyRefreshToken(request.getRefreshToken());
        var jti = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expiryTime(expiryTime)
                .build();

        invalidatedRepository.save(invalidatedToken);

        var userId = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED));

        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .isAuthenticated(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(LogoutRequest request)

            throws JOSEException, ParseException {

        var signedAccessToken = verifyAccessToken(request.getAccessToken());
        var jtiAccessToken = signedAccessToken.getJWTClaimsSet().getJWTID();

        InvalidatedToken invalidatedAccessToken = InvalidatedToken.builder()
                .id(jtiAccessToken)
                .expiryTime(signedAccessToken.getJWTClaimsSet().getExpirationTime())
                .build();
        invalidatedRepository.save(invalidatedAccessToken);

        var signedRefreshToken = verifyRefreshToken(request.getRefreshToken());
        var jtiRefreshToken = signedRefreshToken.getJWTClaimsSet().getJWTID();

        InvalidatedToken invalidatedRefreshToken = InvalidatedToken.builder()
                .id(jtiRefreshToken)
                .expiryTime(signedRefreshToken.getJWTClaimsSet().getExpirationTime())
                .build();
        invalidatedRepository.save(invalidatedRefreshToken);
    }

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {

        var accessToken = request.getAccessToken();

        boolean isValid = true;

        try {
            verifyAccessToken(accessToken);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    private SignedJWT verifyAccessToken(String token)
            throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        var verified = signedJWT.verify(verifier);
        if (!verified) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (!(expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        if (invalidatedRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        return signedJWT;
    }

    private SignedJWT verifyRefreshToken(String token)
            throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        var verified = signedJWT.verify(verifier);
        if (!verified) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (!(expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        if (invalidatedRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        return signedJWT;
    }

    private String generateRefreshToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("acnecare")
                .issueTime(new Date())
                .expirationTime(
                        new Date(
                                Instant.now().plus(refreshTokenValidDuration, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("type", "refresh")
                .build();

        Payload payload = new Payload(claims.toJSONObject());
        JWSObject jwtObject = new JWSObject(header, payload);

        try {
            jwtObject.sign(new MACSigner(signerKey.getBytes()));
            return jwtObject.serialize();

        } catch (JOSEException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_ERROR);
        }
    }

    private String generateAccessToken(User user) {

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("acnecare")
                .issueTime(new Date())
                .expirationTime(
                        new Date(
                                Instant.now().plus(accessTokenValidDuration, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("roles", buildScope(user)) // ROLE_ADMIN READ, WRITE, DELETE
                .claim("type", "access")
                .build();

        Payload payload = new Payload(claims.toJSONObject());

        JWSObject jwtObject = new JWSObject(header, payload);

        try {
            jwtObject.sign(new MACSigner(signerKey.getBytes()));
            return jwtObject.serialize();

        } catch (JOSEException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_ERROR);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

}

// jwt: header, payload, signature

// header: algorithm:SHA512, SHA256

// Payload: subject, issuer, issueTime, expirationTime, jwtID, roles, type

// signature: HMACSHA512(base64UrlEncode(header) + "." +
// base64UrlEncode(payload), secretKey)

//