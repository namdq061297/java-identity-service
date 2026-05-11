package com.example.identify_service.controller;

import com.example.identify_service.dto.request.ApiResponse;
import com.example.identify_service.dto.request.UserCreationRequest;
import com.example.identify_service.dto.request.UserUpdateRequest;
import com.example.identify_service.dto.response.UserResponse;
import com.example.identify_service.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
  UserService userService;

  @PostMapping
  public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest user) {
    ApiResponse<UserResponse> response = new ApiResponse<>();
    response.setResult(userService.createUser(user));
    return response;
  }

  @GetMapping
  public List<UserResponse> getAllUsers() {
    return userService.getUsers();
  }

  @GetMapping("/{id}")
  public UserResponse getUserById(@PathVariable String id) {
    return userService.getUserById(id);
  }

  @PutMapping("/{id}")
  public ApiResponse<UserResponse> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest user) {
    ApiResponse<UserResponse> response = new ApiResponse<>();
    response.setResult(userService.updateUser(id, user));
    return response;
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
  }
}
