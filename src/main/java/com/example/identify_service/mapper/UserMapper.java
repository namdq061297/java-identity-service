package com.example.identify_service.mapper;

import com.example.identify_service.dto.request.UserCreationRequest;
import com.example.identify_service.dto.request.UserUpdateRequest;
import com.example.identify_service.dto.response.UserResponse;
import com.example.identify_service.entity.Role;
import com.example.identify_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toUserDTO(UserCreationRequest user);

  //  @Mapping(source = "firstName", target = "lastName") map firstName=>lastName
  //  @Mapping(target = "firstName", ignore = true)
  UserResponse toUserResponse(User user);

  void updateUser(@MappingTarget User user, UserUpdateRequest req);

  default Set<String> mapRolesToRoleNames(Set<Role> roles) {
    if (roles == null) {
      return null;
    }
    return roles.stream()
        .map(Role::getName)
        .collect(Collectors.toSet());
  }
}
