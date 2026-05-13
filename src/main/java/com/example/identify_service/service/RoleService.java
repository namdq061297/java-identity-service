package com.example.identify_service.service;

import com.example.identify_service.dto.request.RoleRequest;
import com.example.identify_service.dto.response.RoleResponse;
import com.example.identify_service.entity.Role;
import com.example.identify_service.mapper.RoleMapper;
import com.example.identify_service.repository.PermissionRepository;
import com.example.identify_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
  RoleRepository roleRepository;
  RoleMapper roleMapper;
  PermissionRepository permissionRepository;

  public RoleResponse create(RoleRequest create) {
    Role role = roleMapper.toRole(create);
    var permissions = permissionRepository.findAllById(create.getPermissions());
    role.setPermissions(new HashSet<>(permissions));
    role = roleRepository.save(role);
    return roleMapper.toResponse(role);
  }

  public List<RoleResponse> getAll() {
    var roles = roleRepository.findAll();
    return roles.stream().map(roleMapper::toResponse).toList();
  }

  public void delete(String roleId) {
    roleRepository.deleteById(roleId);
  }
}
