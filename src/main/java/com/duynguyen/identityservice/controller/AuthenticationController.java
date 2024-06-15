package com.duynguyen.identityservice.controller;

import com.duynguyen.identityservice.dto.request.AuthenticationRequest;
import com.duynguyen.identityservice.dto.request.IntrospectRequest;
import com.duynguyen.identityservice.dto.response.ApiResponse;
import com.duynguyen.identityservice.dto.response.AuthenticationResponse;
import com.duynguyen.identityservice.dto.response.IntrospectResponse;
import com.duynguyen.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspectToken(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}
