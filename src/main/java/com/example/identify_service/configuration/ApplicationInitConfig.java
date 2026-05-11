package com.example.identify_service.configuration;

import com.example.identify_service.entity.User;
import com.example.identify_service.enums.Roles;
import com.example.identify_service.repository.UserRepository;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {
  private final PasswordEncoder passwordEncoder;

  @Bean
  ApplicationRunner init(UserRepository userRepository) {
    return args -> {
      if (userRepository.findByUsername("admin").isEmpty()) {
        var roles = new HashSet<String>();
        roles.add(Roles.ADMIN.name());
        User user = User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin"))
            .roles(roles)
            .build();
        userRepository.save(user);
        log.warn("Admin has been created");
      }
    };
  }
}
