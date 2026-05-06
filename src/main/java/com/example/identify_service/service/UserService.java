package com.example.identify_service.service;

import com.example.identify_service.dto.request.UserCreationRequest;
import com.example.identify_service.dto.request.UserUpdateRequest;
import com.example.identify_service.dto.response.UserResponse;
import com.example.identify_service.entity.User;
import com.example.identify_service.exception.AppException;
import com.example.identify_service.exception.ErrorCode;
import com.example.identify_service.mapper.UserMapper;
import com.example.identify_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
  UserRepository userRepository;
  UserMapper userMapper;

  public UserResponse createUser(UserCreationRequest req) {
    if (userRepository.existsByUsername(req.getUsername())) {
      throw new AppException(ErrorCode.USER_EXISTED);
    }

    User user = userMapper.toUserDTO(req);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    user.setPassword(passwordEncoder.encode(req.getPassword()));

    return userMapper.toUserResponse(userRepository.save(user));
  }

  public List<User> getUsers() {
    return userRepository.findAll();
  }

  public UserResponse getUserById(String id) {
    return userMapper.toUserResponse(userRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
  }

  public UserResponse updateUser(String id, @NonNull UserUpdateRequest req) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    //    user.setFirstName(req.getFirstName());
    //    user.setLastName(req.getLastName());
    //    user.setDob(req.getDob());

    userMapper.updateUser(user, req);

    return userMapper.toUserResponse(userRepository.save(user));
  }

  public void deleteUser(String id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    userRepository.delete(user);
  }
}
