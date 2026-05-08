package com.example.identify_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Value("${jwt.signerKey}")
  private String SIGNER_KEY;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) {
    final String[] PUBLIC_ENDPOINT = {"/users", "/auth/token", "/auth/introspect"};
    http.authorizeHttpRequests(
        req ->
            req.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT)
                .permitAll()
                .anyRequest()
                .authenticated());
    http.oauth2ResourceServer(auth2 -> auth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())));
    http.csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }

  @Bean
  JwtDecoder jwtDecoder() {
    SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(),
        "HS512");
    return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
  }
}
