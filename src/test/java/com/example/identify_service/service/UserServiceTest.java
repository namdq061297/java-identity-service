package com.example.identify_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.identify_service.dto.request.UserCreationRequest;
import com.example.identify_service.dto.response.UserResponse;
import com.example.identify_service.entity.User;
import com.example.identify_service.exception.AppException;
import com.example.identify_service.exception.ErrorCode;
import com.example.identify_service.mapper.UserMapper;
import com.example.identify_service.repository.UserRepository;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Test
  void createUserMapsDobFromRequest() {
    UserService userService = new UserService(userRepository, userMapper, passwordEncoder);

    UserCreationRequest request = new UserCreationRequest();
    request.setUsername("jane");
    request.setPassword("secret");
    request.setFirstName("Jane");
    request.setLastName("Doe");
    request.setDob(LocalDate.of(2000, 1, 2));

    User userEntity = new User();
    userEntity.setUsername("jane");
    userEntity.setPassword("secret");
    userEntity.setFirstName("Jane");
    userEntity.setLastName("Doe");
    userEntity.setDob(LocalDate.of(2000, 1, 2));

    UserResponse expectedResponse = new UserResponse();
    expectedResponse.setUsername("jane");
    expectedResponse.setFirstName("Jane");
    expectedResponse.setLastName("Doe");
    expectedResponse.setDob(LocalDate.of(2000, 1, 2));
    expectedResponse.setRoles(Set.of("USER"));

    when(userRepository.existsByUsername("jane")).thenReturn(false);
    when(userMapper.toUserDTO(request)).thenReturn(userEntity);
    when(passwordEncoder.encode("secret")).thenReturn("encoded-secret");
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(userMapper.toUserResponse(any(User.class))).thenReturn(expectedResponse);

    UserResponse createdUser = userService.createUser(request);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).existsByUsername("jane");
    verify(passwordEncoder).encode("secret");
    verify(userRepository).save(userCaptor.capture());
    assertThat(userCaptor.getValue().getPassword()).isEqualTo("encoded-secret");
    assertThat(userCaptor.getValue().getDob()).isEqualTo(LocalDate.of(2000, 1, 2));
    assertThat(createdUser.getDob()).isEqualTo(LocalDate.of(2000, 1, 2));
    assertThat(createdUser.getRoles()).containsExactly("USER");
  }

  @Test
  void createUserThrowsWhenUsernameAlreadyExists() {
    UserService userService = new UserService(userRepository, userMapper, passwordEncoder);

    UserCreationRequest request = new UserCreationRequest();
    request.setUsername("jane");
    request.setPassword("secret");
    request.setFirstName("Jane");
    request.setLastName("Doe");
    request.setDob(LocalDate.of(2000, 1, 2));

    when(userRepository.existsByUsername("jane")).thenReturn(true);

    assertThatThrownBy(() -> userService.createUser(request))
        .isInstanceOfSatisfying(AppException.class, ex -> {
          assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_EXISTED);
          assertThat(ex.getMessage()).isEqualTo(ErrorCode.USER_EXISTED.getMessage());
        });

    verify(userRepository).existsByUsername("jane");
    verify(userRepository, never()).save(any(User.class));
  }
}
