package com.example.identify_service.exception;

public enum ErrorCode {
  UNCATEGORIZED_ERROR(9999, "Uncategorized error"),
  USER_NOT_FOUND(1001, "User not found"),
  USER_EXISTED(1002, "Username already exists"),
  INVALID_PASSWORD(1003, "Password must be at least 8 characters long"),
  INVALID_REQUEST(1004, "Invalid request body"),
  USERNAME_REQUIRED(1005, "Username is required"),
  PASSWORD_REQUIRED(1006, "Password is required"),
  FIRST_NAME_REQUIRED(1007, "First name is required"),
  LAST_NAME_REQUIRED(1008, "Last name is required"),
  USERNAME_TOO_LONG(1009, "Username must be not over 20 characters long"),

  ;
  private int code;
  private String message;

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
