package com.example.identify_service.mapper;

import com.example.identify_service.dto.request.UserCreationRequest;
import com.example.identify_service.dto.request.UserUpdateRequest;
import com.example.identify_service.dto.response.UserResponse;
import com.example.identify_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toUserDTO(UserCreationRequest user);

  UserResponse toUserResponse(User user);

  void updateUser(@MappingTarget User user, UserUpdateRequest req);
}
