package com.acnecare.api.auth.service;

import com.acnecare.api.auth.dto.request.AuthenticationRequest;
import com.acnecare.api.auth.dto.request.ExchangeTokenRequest;
import com.acnecare.api.auth.dto.request.IntrospectRequest;
import com.acnecare.api.auth.dto.request.LogoutRequest;
import com.acnecare.api.auth.dto.request.RefreshRequest;
import com.acnecare.api.auth.dto.response.AuthenticationResponse;
import com.acnecare.api.auth.dto.response.IntrospectResponse;
import com.acnecare.api.auth.entity.InvalidatedToken;
import com.acnecare.api.auth.repository.InvalidatedRepository;
import com.acnecare.api.auth.repository.OutboundIdentityClient;
import com.acnecare.api.auth.repository.OutboundUserInfoClient;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.role.entity.Role;
import com.acnecare.api.role.reposity.RoleReposity;
import com.acnecare.api.patient.entity.PatientProfile;
import com.acnecare.api.patient.repository.PatientProfileRepository;
import com.acnecare.api.user.entity.User;
import com.acnecare.api.user.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    @NonFinal
    @Value("${jwt.signer-key}")
    protected String signerKey;

    @NonFinal
    @Value("${jwt.access-token-valid-duration}")
    protected long accessTokenValidDuration;

    @NonFinal
    @Value("${jwt.refresh-token-valid-duration}")
    protected long refreshTokenValidDuration;

    UserRepository userRepository;
    InvalidatedRepository invalidatedRepository;
    RoleReposity roleReposity;
    PatientProfileRepository patientProfileRepository;
    OutboundIdentityClient outboundIdentityClient;
    OutboundUserInfoClient outboundUserInfoClient;

    @NonFinal
    @Value("${outbound.identity.client-id}")
    protected String googleClientId;

    @NonFinal
    @Value("${outbound.identity.client-secret}")
    protected String googleClientSecret;

    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    protected String googleRedirectUri;

    @NonFinal
    protected final String authorizationCodeGrantType = "authorization_code";

    public AuthenticationResponse authenticate(AuthenticationRequest request)
            throws JOSEException, ParseException, AppException {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if ("BLOCK".equals(user.getStatus())) {
            throw new AppException(ErrorCode.USER_IS_BLOCKED);
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new AppException(ErrorCode.PASSWORD_LOGIN_NOT_ENABLED);
        }
        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isAuthenticated) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        return authenticateUser(user);
    }

    public AuthenticationResponse outboundAuthenticate(String code) {
        if (googleClientId == null || googleClientId.isBlank()
                || googleClientSecret == null || googleClientSecret.isBlank()
                || googleRedirectUri == null || googleRedirectUri.isBlank()) {
            log.error("Google OAuth config missing. clientIdSet={}, clientSecretSet={}, redirectUriSet={}",
                    googleClientId != null && !googleClientId.isBlank(),
                    googleClientSecret != null && !googleClientSecret.isBlank(),
                    googleRedirectUri != null && !googleRedirectUri.isBlank());
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        ExchangeTokenRequest request = ExchangeTokenRequest.builder()
                .code(code)
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .redirectUri(googleRedirectUri)
                .grantType(authorizationCodeGrantType)
                .build();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", request.getCode());
        requestBody.add("client_id", request.getClientId());
        requestBody.add("client_secret", request.getClientSecret());
        requestBody.add("redirect_uri", request.getRedirectUri());
        requestBody.add("grant_type", request.getGrantType());

        try {
            var tokenResponse = outboundIdentityClient.exchangeToken(requestBody);
            var googleUserInfo = outboundUserInfoClient.getUserInfo("Bearer " + tokenResponse.getAccessToken());

            if (googleUserInfo.getEmail() == null || googleUserInfo.getEmail().isBlank()) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            User user = findOrCreateOAuthUser(googleUserInfo);
            return authenticateUser(user);
        } catch (AppException exception) {
            throw exception;
        } catch (Exception exception) {
            log.error("Google outbound authentication failed: {}", exception.getMessage(), exception);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public AuthenticationResponse refreshToken(RefreshRequest request)
            throws JOSEException, ParseException, AppException {

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

        return authenticateUser(user);
    }

    public void logout(LogoutRequest request)
            throws JOSEException, ParseException, AppException {

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

    private AuthenticationResponse authenticateUser(User user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .isAuthenticated(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private synchronized User findOrCreateOAuthUser(com.acnecare.api.auth.dto.response.GoogleUserInfoResponse googleUserInfo) {
        List<User> usersByGoogleSub = userRepository.findAllByGoogleSub(googleUserInfo.getSub());
        if (usersByGoogleSub.size() > 1) {
            log.warn("Multiple users found for googleSub={} count={}. Using most recently updated user.",
                    googleUserInfo.getSub(), usersByGoogleSub.size());
        }
        User userByGoogleSub = pickMostRecentUser(usersByGoogleSub);
        if (userByGoogleSub != null) {
            return updateOAuthIdentity(userByGoogleSub, googleUserInfo);
        }

        List<User> usersByEmail = userRepository.findAllByEmail(googleUserInfo.getEmail());
        if (usersByEmail.size() > 1) {
            log.warn("Multiple users found for email={} count={}. Using most recently updated user.",
                    googleUserInfo.getEmail(), usersByEmail.size());
        }
        User userByEmail = pickMostRecentUser(usersByEmail);
        if (userByEmail != null) {
            return updateOAuthIdentity(userByEmail, googleUserInfo);
        }

        return createOAuthUser(googleUserInfo);
    }

    private User updateOAuthIdentity(User user, com.acnecare.api.auth.dto.response.GoogleUserInfoResponse googleUserInfo) {
        if ("BLOCK".equals(user.getStatus())) {
            throw new AppException(ErrorCode.USER_IS_BLOCKED);
        }

        user.setGoogleSub(googleUserInfo.getSub());
        String resolvedFirstName = resolveFirstName(googleUserInfo);
        String resolvedLastName = resolveLastName(googleUserInfo);

        if ((user.getFirstName() == null || user.getFirstName().isBlank())
                && resolvedFirstName != null && !resolvedFirstName.isBlank()) {
            user.setFirstName(resolvedFirstName);
        }
        if ((user.getLastName() == null || user.getLastName().isBlank())
                && resolvedLastName != null && !resolvedLastName.isBlank()) {
            user.setLastName(resolvedLastName);
        }
        if ((user.getAvatarUrl() == null || user.getAvatarUrl().isBlank())
                && googleUserInfo.getPicture() != null && !googleUserInfo.getPicture().isBlank()) {
            user.setAvatarUrl(googleUserInfo.getPicture());
        }
        if (user.getStatus() == null || user.getStatus().isBlank()) {
            user.setStatus("ACTIVE");
        }
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setLastLoginAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        ensurePatientProfile(savedUser);
        return savedUser;
    }

    private User createOAuthUser(com.acnecare.api.auth.dto.response.GoogleUserInfoResponse googleUserInfo) {
        Role patientRole = roleReposity.findById("PATIENT")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        String resolvedFirstName = resolveFirstName(googleUserInfo);
        String resolvedLastName = resolveLastName(googleUserInfo);

        User user = User.builder()
                .email(googleUserInfo.getEmail())
                .googleSub(googleUserInfo.getSub())
                .firstName(resolvedFirstName)
                .lastName(resolvedLastName)
                .avatarUrl(googleUserInfo.getPicture())
                .status("ACTIVE")
                .roles(Set.of(patientRole))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        ensurePatientProfile(savedUser);
        return savedUser;
    }

    private User pickMostRecentUser(List<User> users) {
        if (users == null || users.isEmpty()) {
            return null;
        }

        return users.stream()
                .max(Comparator
                        .comparing((User u) -> u.getUpdatedAt() == null ? LocalDateTime.MIN : u.getUpdatedAt())
                        .thenComparing(u -> u.getCreatedAt() == null ? LocalDateTime.MIN : u.getCreatedAt()))
                .orElse(users.get(0));
    }

    private String resolveFirstName(com.acnecare.api.auth.dto.response.GoogleUserInfoResponse googleUserInfo) {
        if (googleUserInfo.getGivenName() != null && !googleUserInfo.getGivenName().isBlank()) {
            return googleUserInfo.getGivenName().trim();
        }

        String fullName = googleUserInfo.getName();
        if (fullName == null || fullName.isBlank()) {
            return "Google";
        }

        String[] parts = fullName.trim().split("\\s+");
        if (parts.length <= 1) {
            return parts[0];
        }

        StringBuilder firstNameBuilder = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            if (i > 0) {
                firstNameBuilder.append(" ");
            }
            firstNameBuilder.append(parts[i]);
        }
        return firstNameBuilder.toString();
    }

    private String resolveLastName(com.acnecare.api.auth.dto.response.GoogleUserInfoResponse googleUserInfo) {
        if (googleUserInfo.getFamilyName() != null && !googleUserInfo.getFamilyName().isBlank()) {
            return googleUserInfo.getFamilyName().trim();
        }

        String fullName = googleUserInfo.getName();
        if (fullName == null || fullName.isBlank()) {
            return "User";
        }

        String[] parts = fullName.trim().split("\\s+");
        return parts[parts.length - 1];
    }

    private void ensurePatientProfile(User user) {
        if (user == null || user.getId() == null || user.getId().isBlank()) {
            return;
        }

        boolean isPatient = user.getRoles() != null
                && user.getRoles().stream().anyMatch(role -> "PATIENT".equals(role.getName()));
        if (!isPatient) {
            return;
        }

        if (!patientProfileRepository.existsByUserId(user.getId())) {
            patientProfileRepository.save(PatientProfile.builder()
                    .user(user)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build());
        }
    }

}
