package com.example.identify_service.controller;

import com.example.identify_service.dto.request.UserCreationRequest;
import com.example.identify_service.dto.request.UserUpdateRequest;
import com.example.identify_service.entity.User;
import com.example.identify_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserService userService;

  @PostMapping
  public User createUser(@RequestBody @Valid UserCreationRequest user) {
    return userService.createUser(user);
  }

  @GetMapping
  public List<User> getAllUsers() {
    return userService.getUsers();
  }

  @GetMapping("/{id}")
  public User getUserById(@PathVariable String id) {
    return userService.getUserById(id);
  }

  @PutMapping("/{id}")
  public User updateUser(@PathVariable String id, @RequestBody UserUpdateRequest user) {
    return userService.updateUser(id, user);
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
  }

}
