package com.example.identify_service.controller;

import com.example.identify_service.dto.request.*;
import com.example.identify_service.dto.response.AuthenticationResponse;
import com.example.identify_service.dto.response.IntrospectResponse;
import com.example.identify_service.dto.response.LogoutResponse;
import com.example.identify_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
  AuthenticationService authenticationService;

  @PostMapping("/token")
  ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
    AuthenticationResponse result = authenticationService.authenticate(request);
    return ApiResponse.<AuthenticationResponse>builder()
        .result(result)
        .build();
  }

  @PostMapping("/introspect")
  ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
    IntrospectResponse result = authenticationService.introspect(request);
    return ApiResponse.<IntrospectResponse>builder()
        .result(result)
        .build();
  }

  @PostMapping("/logout")
  ApiResponse<LogoutResponse> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
    LogoutResponse result = authenticationService.logout(request);
    return ApiResponse.<LogoutResponse>builder()
        .result(result)
        .build();
  }

  @PostMapping("/refreshToken")
  ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
    AuthenticationResponse result = authenticationService.refreshToken(request);
    return ApiResponse.<AuthenticationResponse>builder()
        .result(result)
        .build();
  }
}
