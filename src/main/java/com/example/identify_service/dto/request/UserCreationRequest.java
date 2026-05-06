package com.example.identify_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UserCreationRequest {
  @NotBlank(message = "USERNAME_REQUIRED")
  @Size(max = 20, message = "USERNAME_TOO_LONG")
  private String username;
  @NotBlank(message = "PASSWORD_REQUIRED")
  @Size(min = 8, message = "INVALID_PASSWORD")
  private String password;
  @NotBlank(message = "FIRST_NAME_REQUIRED")
  private String firstName;
  @NotBlank(message = "LAST_NAME_REQUIRED")
  private String lastName;
  private LocalDate dob;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public LocalDate getDob() {
    return dob;
  }

  public void setDob(LocalDate dob) {
    this.dob = dob;
  }
}
