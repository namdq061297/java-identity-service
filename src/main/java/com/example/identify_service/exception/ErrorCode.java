package com.example.identify_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Locale;

@Getter
public enum ErrorCode {
  UNCATEGORIZED_ERROR(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_MESSAGE_KEY(1111, "Invalid message key", HttpStatus.BAD_REQUEST),
  USER_NOT_FOUND(1001, "User not found", HttpStatus.NOT_FOUND),
  USER_EXISTED(1002, "Username already exists".toLowerCase(Locale.ROOT), HttpStatus.BAD_REQUEST),
  INVALID_PASSWORD(1003, "Password must be at least 8 characters long", HttpStatus.BAD_REQUEST),
  INVALID_REQUEST(1004, "Invalid request body", HttpStatus.BAD_REQUEST),
  USERNAME_REQUIRED(1005, "Username is required", HttpStatus.BAD_REQUEST),
  PASSWORD_REQUIRED(1006, "Password is required", HttpStatus.BAD_REQUEST),
  FIRST_NAME_REQUIRED(1007, "First name is required", HttpStatus.BAD_REQUEST),
  LAST_NAME_REQUIRED(1008, "Last name is required", HttpStatus.BAD_REQUEST),
  USERNAME_TOO_LONG(1009, "Username must be not over 20 characters long", HttpStatus.BAD_REQUEST),
  UNAUTHENTICATED(1010, "Unauthenticated", HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(1011, "Unauthorized", HttpStatus.FORBIDDEN),
  INVALID_DOB(1012, "Invalid date of birth", HttpStatus.BAD_REQUEST);
  private int code;
  private String message;
  private HttpStatusCode httpsStatusCode;

  ErrorCode(int code, String message, HttpStatusCode httpsStatusCode) {
    this.code = code;
    this.message = message;
    this.httpsStatusCode = httpsStatusCode;
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
