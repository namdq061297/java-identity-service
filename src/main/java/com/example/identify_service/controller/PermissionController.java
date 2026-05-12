package com.example.identify_service.controller;

import com.example.identify_service.dto.request.ApiResponse;
import com.example.identify_service.dto.request.PermissionRequest;
import com.example.identify_service.dto.response.PermissionResponse;
import com.example.identify_service.service.PermissionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
  PermissionService permissionService;

  @PostMapping
  public ApiResponse<PermissionResponse> createPermission(@RequestBody @Valid PermissionRequest permissionRequest) {
    ApiResponse<PermissionResponse> response = new ApiResponse<>();
    log.info("Creating permission: {}", permissionRequest.getName());
    response.setResult(permissionService.create(permissionRequest));
    return response;
  }

  @GetMapping
  public List<PermissionResponse> getAllPermissions() {
    return permissionService.getAll();
  }

  @DeleteMapping("/{id}")
  public void deletePermission(@PathVariable String id) {
    permissionService.delete(id);
  }
}
