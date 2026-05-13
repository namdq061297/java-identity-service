package com.example.identify_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.identify_service.dto.request.RoleRequest;
import com.example.identify_service.dto.response.RoleResponse;
import com.example.identify_service.entity.Permission;
import com.example.identify_service.entity.Role;
import com.example.identify_service.mapper.RoleMapper;
import com.example.identify_service.repository.PermissionRepository;
import com.example.identify_service.repository.RoleRepository;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private RoleMapper roleMapper;

  @Mock
  private PermissionRepository permissionRepository;

  @Test
  void createLoadsPermissionsAndSavesRole() {
    RoleService roleService = new RoleService(roleRepository, roleMapper, permissionRepository);

    RoleRequest request = RoleRequest.builder()
        .name("ADMIN")
        .description("Administrator")
        .permissions(Set.of("CREATE_USER", "READ_USER"))
        .build();

    Role mappedRole = Role.builder()
        .name("ADMIN")
        .description("Administrator")
        .build();

    Permission createUser = Permission.builder().name("CREATE_USER").description("Create user").build();
    Permission readUser = Permission.builder().name("READ_USER").description("Read user").build();

    Role savedRole = Role.builder()
        .name("ADMIN")
        .description("Administrator")
        .permissions(Set.of(createUser, readUser))
        .build();

    RoleResponse response = RoleResponse.builder()
        .name("ADMIN")
        .description("Administrator")
        .permissions(Set.of("CREATE_USER", "READ_USER"))
        .build();

    when(roleMapper.toRole(request)).thenReturn(mappedRole);
    when(permissionRepository.findAllById(request.getPermissions())).thenReturn(List.of(createUser, readUser));
    when(roleRepository.save(mappedRole)).thenReturn(savedRole);
    when(roleMapper.toResponse(savedRole)).thenReturn(response);

    RoleResponse created = roleService.create(request);

    assertThat(mappedRole.getPermissions()).containsExactlyInAnyOrder(createUser, readUser);
    assertThat(created).isSameAs(response);
    verify(roleMapper).toRole(request);
    verify(permissionRepository).findAllById(request.getPermissions());
    verify(roleRepository).save(mappedRole);
    verify(roleMapper).toResponse(savedRole);
  }
}

