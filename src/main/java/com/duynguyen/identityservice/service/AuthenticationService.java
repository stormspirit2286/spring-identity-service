package com.duynguyen.identityservice.service;

import com.duynguyen.identityservice.dto.request.AuthenticationRequest;
import com.duynguyen.identityservice.dto.response.AuthenticationResponse;
import com.duynguyen.identityservice.exception.AppException;
import com.duynguyen.identityservice.exception.ErrorCode;
import com.duynguyen.identityservice.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    @NonFinal
    protected static final String SIGNER_KEY = "daqURcF8qCLgmiROhYwzS2CnxwPqymRzyLeQ1ExYFyhNJh0BKkoZn1EZcY8AprEo\n";
    UserRepository userRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(request.getUsername());
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    private String generateToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().subject(username).issuer("NguyenVanDuy").issueTime(new Date()).expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())).claim("customClaim", "Custom").build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jws0bject = new JWSObject(header, payload);

        try {
            jws0bject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jws0bject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token: ", e);
            throw new RuntimeException(e);
        }

    }
}
