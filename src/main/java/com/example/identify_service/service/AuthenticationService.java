package com.example.identify_service.service;

import com.example.identify_service.dto.request.AuthenticationRequest;
import com.example.identify_service.dto.request.IntrospectRequest;
import com.example.identify_service.dto.request.LogoutRequest;
import com.example.identify_service.dto.response.AuthenticationResponse;
import com.example.identify_service.dto.response.IntrospectResponse;
import com.example.identify_service.dto.response.LogoutResponse;
import com.example.identify_service.entity.InvalidatedToken;
import com.example.identify_service.entity.User;
import com.example.identify_service.exception.AppException;
import com.example.identify_service.exception.ErrorCode;
import com.example.identify_service.repository.InvalidatedTokenRepository;
import com.example.identify_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
  @NonFinal
  @Value("${jwt.signerKey}")
  protected String SIGNER_KEY;

  UserRepository userRepository;
  InvalidatedTokenRepository invalidatedTokenRepository;

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    var user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
    if (isAuthenticated) {
      var token = generateToken(user);
      return AuthenticationResponse.builder()
          .token(token)
          .isAuthenticated(true)
          .build();
    } else {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }
  }

  private String generateToken(User user) {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(user.getUsername())
        .issuer("com.example.identify_service")
        .issueTime(new Date())
        .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000))
        .jwtID(String.valueOf(UUID.randomUUID()))// 1 hour expiration
        .claim("scope", buildScrope(user))
        .build();
    Payload payload = new Payload(claimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(header, payload);
    try {
      jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      throw new AppException(ErrorCode.UNCATEGORIZED_ERROR);
    }
  }

  public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
    boolean isValid = true;
    try {
      verifyToken(request.getToken());
    } catch (AppException e) {
      isValid = false;
    }
    return IntrospectResponse.builder().isValid(isValid).build();
  }

  private String buildScrope(User user) {
    StringJoiner stringJoiner = new StringJoiner(" ");
    if (!CollectionUtils.isEmpty(user.getRoles())) {
      user.getRoles().forEach(role -> {
        stringJoiner.add("ROLE_" + role.getName());
        if (!CollectionUtils.isEmpty(role.getPermissions())) {
          role.getPermissions().forEach(permission -> {
            stringJoiner.add("PERMISSION_" + permission.getName());
          });
        }

      });
      return stringJoiner.toString();
    }
    return "";
  }

  public LogoutResponse logout(LogoutRequest request) throws ParseException, JOSEException {
    SignedJWT signedJWT = verifyToken(request.getToken());

    String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
    Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

    invalidatedTokenRepository.save(InvalidatedToken.builder()
        .id(jwtId)
        .expiryTime(expiryTime)
        .build());

    return LogoutResponse.builder()
        .success(true)
        .build();
  }

  private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
    JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
    SignedJWT signedJWT = SignedJWT.parse(token);

    Date expireToken = signedJWT.getJWTClaimsSet().getExpirationTime();
    String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
    var isVerified = signedJWT.verify(verifier);

    if (!isVerified || expireToken == null || expireToken.before(new Date())
        || jwtId == null || invalidatedTokenRepository.existsById(jwtId)) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    return signedJWT;
  }
}
