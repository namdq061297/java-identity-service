package com.example.identify_service.service;

import com.example.identify_service.dto.request.UserCreationRequest;
import com.example.identify_service.dto.request.UserUpdateRequest;
import com.example.identify_service.dto.response.UserResponse;
import com.example.identify_service.entity.User;
import com.example.identify_service.enums.Roles;
import com.example.identify_service.exception.AppException;
import com.example.identify_service.exception.ErrorCode;
import com.example.identify_service.mapper.UserMapper;
import com.example.identify_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
  final UserRepository userRepository;
  final UserMapper userMapper;
  final PasswordEncoder passwordEncoder;

  public UserResponse createUser(UserCreationRequest req) {
    if (userRepository.existsByUsername(req.getUsername())) {
      throw new AppException(ErrorCode.USER_EXISTED);
    }

    User user = userMapper.toUserDTO(req);
    user.setPassword(passwordEncoder.encode(req.getPassword()));

    HashSet<String> roles = new HashSet<>();
    roles.add(Roles.USER.name());
    user.setRoles(roles);

    return userMapper.toUserResponse(userRepository.save(user));
  }

  public List<UserResponse> getUsers() {
    return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
  }

  public UserResponse getUserById(String id) {
    return userMapper.toUserResponse(userRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
  }

  @PostAuthorize("hasRole('ADMIN') or (returnObject != null and returnObject.username == authentication.name)")
  public UserResponse updateUser(String id, @NonNull UserUpdateRequest req) {
    log.info("Update User with id {}", id);
    User user = userRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    userMapper.updateUser(user, req);

    return userMapper.toUserResponse(userRepository.save(user));
  }

  @PreAuthorize("hasRole('ADMIN')")
  public void deleteUser(String id) {
    log.warn("Deleting user");
    User user = userRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    userRepository.delete(user);
  }

  public UserResponse getMyInfo() {
    var context = SecurityContextHolder.getContext();
    var name = context.getAuthentication().getName();
    User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    return userMapper.toUserResponse(user);
  }
}
