package com.example.identify_service.mapper;

import com.example.identify_service.dto.request.UserCreationRequest;
import com.example.identify_service.dto.request.UserUpdateRequest;
import com.example.identify_service.dto.response.UserResponse;
import com.example.identify_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
  User toUserDTO(UserCreationRequest user);

  //  @Mapping(source = "firstName", target = "lastName") map firstName=>lastName
  //  @Mapping(target = "firstName", ignore = true)
  UserResponse toUserResponse(User user);

  @Mapping(target = "roles", ignore = true)
  void updateUser(@MappingTarget User user, UserUpdateRequest req);

}
