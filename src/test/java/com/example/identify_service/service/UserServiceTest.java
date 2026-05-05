package com.example.identify_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import com.example.identify_service.dto.request.UserCreationRequest;
import com.example.identify_service.entity.User;
import com.example.identify_service.exception.AppException;
import com.example.identify_service.exception.ErrorCode;
import com.example.identify_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void createUserMapsDobFromRequest() {
        UserService userService = new UserService();
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);

        UserCreationRequest request = new UserCreationRequest();
        request.setUsername("jane");
        request.setPassword("secret");
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setDob(LocalDate.of(2000, 1, 2));

        when(userRepository.existsByUsername("jane")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = userService.createUser(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).existsByUsername("jane");
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getDob()).isEqualTo(LocalDate.of(2000, 1, 2));
        assertThat(createdUser.getDob()).isEqualTo(LocalDate.of(2000, 1, 2));
    }

    @Test
    void createUserThrowsWhenUsernameAlreadyExists() {
        UserService userService = new UserService();
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);

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
                    assertThat(ex.getMessage()).isEqualTo("Username already exists");
                });

        verify(userRepository).existsByUsername("jane");
        verify(userRepository, never()).save(any(User.class));
    }
}
