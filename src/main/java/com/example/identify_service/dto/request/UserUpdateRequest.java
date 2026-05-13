package com.example.identify_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
  @NotBlank(message = "FIRST_NAME_REQUIRED")
  String firstName;
  @NotBlank(message = "LAST_NAME_REQUIRED")
  String lastName;
  LocalDate dob;
  List<String> roles;
}
