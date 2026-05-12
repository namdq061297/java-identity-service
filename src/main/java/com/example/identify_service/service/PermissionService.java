package com.example.identify_service.service;

import com.example.identify_service.dto.request.PermissionRequest;
import com.example.identify_service.dto.response.PermissionResponse;
import com.example.identify_service.entity.Permission;
import com.example.identify_service.mapper.PermissionMapper;
import com.example.identify_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
  PermissionRepository permissionRepository;
  PermissionMapper permissionMapper;

  public PermissionResponse create(PermissionRequest create) {
    Permission permission = permissionMapper.toPermission(create);
    permission = permissionRepository.save(permission);
    return permissionMapper.toResponse(permission);
  }

  public List<PermissionResponse> getAll() {
    var permission = permissionRepository.findAll();
    return permission.stream().map(permissionMapper::toResponse).toList();
  }

  public void delete(String permissionId) {
    permissionRepository.deleteById(permissionId);
  }
}
