package com.example.identify_service.dto.request;

import com.example.identify_service.validator.DobConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
  @NotBlank(message = "USERNAME_REQUIRED")
  @Size(max = 20, message = "USERNAME_TOO_LONG")
  String username;
  @NotBlank(message = "PASSWORD_REQUIRED")
  @Size(min = 8, message = "INVALID_PASSWORD")
  String password;
  @NotBlank(message = "FIRST_NAME_REQUIRED")
  String firstName;
  @NotBlank(message = "LAST_NAME_REQUIRED")
  String lastName;
  @DobConstraint(message = "INVALID_DOB", minYearOld = 10)
  LocalDate dob;
}
