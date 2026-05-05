package com.example.identify_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserExceptions.UserNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleUserNotFound(UserExceptions.UserNotFoundException ex) {
    return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(UserExceptions.UsernameAlreadyExistsException.class)
  public ResponseEntity<Map<String, Object>> handleUsernameAlreadyExists(UserExceptions.UsernameAlreadyExistsException ex) {
    return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationError(MethodArgumentNotValidException ex) {
    String message = "Invalid request body";
    if (ex.getBindingResult().getFieldError() != null && ex.getBindingResult().getFieldError().getDefaultMessage() != null) {
      message = ex.getBindingResult().getFieldError().getDefaultMessage();
    }
    return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, Object>> handleMalformedBody(HttpMessageNotReadableException ex) {
    return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid request body");
  }

  private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
    return ResponseEntity.status(status).body(Map.of(
        "timestamp", Instant.now().toString(),
        "status", status.value(),
        "error", status.getReasonPhrase(),
        "message", message
    ));
  }
}

