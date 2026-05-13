package com.example.identify_service.mapper;

import com.example.identify_service.dto.request.RoleRequest;
import com.example.identify_service.dto.response.RoleResponse;
import com.example.identify_service.entity.Permission;
import com.example.identify_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {
  @Mapping(target = "permissions", ignore = true)
  Role toRole(RoleRequest request);

  RoleResponse toResponse(Role role);

  default Set<String> mapPermissionsToNames(Set<Permission> permissions) {
    if (permissions == null) {
      return null;
    }

    return permissions.stream()
        .map(Permission::getName)
        .collect(Collectors.toSet());
  }
}
