package com.example.identify_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.identify_service.dto.request.PermissionRequest;
import com.example.identify_service.dto.response.PermissionResponse;
import com.example.identify_service.entity.Permission;
import com.example.identify_service.mapper.PermissionMapper;
import com.example.identify_service.repository.PermissionRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

  @Mock
  private PermissionRepository permissionRepository;

  @Mock
  private PermissionMapper permissionMapper;

  @Test
  void createUsesMapperAndRepository() {
    PermissionService permissionService = new PermissionService(permissionRepository, permissionMapper);

    PermissionRequest request = PermissionRequest.builder()
        .name("CREATE_USER")
        .description("Create user permission")
        .build();

    Permission permission = Permission.builder()
        .name("CREATE_USER")
        .description("Create user permission")
        .build();

    PermissionResponse response = PermissionResponse.builder()
        .name("CREATE_USER")
        .description("Create user permission")
        .build();

    when(permissionMapper.toPermission(request)).thenReturn(permission);
    when(permissionRepository.save(permission)).thenReturn(permission);
    when(permissionMapper.toResponse(permission)).thenReturn(response);

    PermissionResponse created = permissionService.create(request);

    assertThat(created).isSameAs(response);
    verify(permissionMapper).toPermission(request);
    verify(permissionRepository).save(permission);
    verify(permissionMapper).toResponse(permission);
  }

  @Test
  void getAllMapsEntitiesToResponses() {
    PermissionService permissionService = new PermissionService(permissionRepository, permissionMapper);

    Permission permission = Permission.builder()
        .name("READ_USER")
        .description("Read user permission")
        .build();
    PermissionResponse response = PermissionResponse.builder()
        .name("READ_USER")
        .description("Read user permission")
        .build();

    when(permissionRepository.findAll()).thenReturn(List.of(permission));
    when(permissionMapper.toResponse(any(Permission.class))).thenReturn(response);

    List<PermissionResponse> permissions = permissionService.getAll();

    assertThat(permissions).containsExactly(response);
    verify(permissionRepository).findAll();
    verify(permissionMapper).toResponse(permission);
  }
}

