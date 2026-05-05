package com.example.identify_service.exception;

public enum ErrorCode {
  UNCATEGORIZED_ERROR(9999, "Uncategorized error"),
  USER_NOT_FOUND(1001, "User not found"),
  USER_EXISTED(1002, "Username already exists"),

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
