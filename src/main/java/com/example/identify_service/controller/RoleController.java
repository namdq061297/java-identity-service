package com.example.identify_service.controller;

import com.example.identify_service.dto.request.ApiResponse;
import com.example.identify_service.dto.request.RoleRequest;
import com.example.identify_service.dto.response.RoleResponse;
import com.example.identify_service.service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
  RoleService roleService;

  @PostMapping
  public ApiResponse<RoleResponse> createRole(@RequestBody @Valid RoleRequest roleRequest) {
    ApiResponse<RoleResponse> response = new ApiResponse<>();
    response.setResult(roleService.create(roleRequest));
    return response;
  }

  @GetMapping
  public List<RoleResponse> getAllRoles() {
    return roleService.getAll();
  }

  @DeleteMapping("/{id}")
  public void deleteRole(@PathVariable String id) {
    roleService.delete(id);
  }
}

