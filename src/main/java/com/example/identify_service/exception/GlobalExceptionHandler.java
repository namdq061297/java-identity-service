package com.example.identify_service.exception;

import com.example.identify_service.dto.request.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleUncategorizedException(Exception ex) {
    return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
        ErrorCode.UNCATEGORIZED_ERROR.getCode(),
        ErrorCode.UNCATEGORIZED_ERROR.getMessage());
  }

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex) {
    ErrorCode errorCode = ex.getErrorCode();
    HttpStatus status = switch (errorCode) {
      case USER_NOT_FOUND -> HttpStatus.NOT_FOUND;
      case USER_EXISTED -> HttpStatus.CONFLICT;
      default -> HttpStatus.INTERNAL_SERVER_ERROR;
    };
    return buildErrorResponse(status, errorCode.getCode(), errorCode.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidationError(MethodArgumentNotValidException ex) {
    ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
    if (ex.getBindingResult().getFieldError() != null && ex.getBindingResult().getFieldError().getDefaultMessage() != null) {
      errorCode = resolveErrorCode(ex.getBindingResult().getFieldError().getDefaultMessage());
    }
    return buildErrorResponse(HttpStatus.BAD_REQUEST, errorCode.getCode(), errorCode.getMessage());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<Void>> handleMalformedBody(HttpMessageNotReadableException ex) {
    return buildErrorResponse(HttpStatus.BAD_REQUEST,
        ErrorCode.INVALID_REQUEST.getCode(),
        ErrorCode.INVALID_REQUEST.getMessage());
  }

  private ErrorCode resolveErrorCode(String errorKey) {
    try {
      return ErrorCode.valueOf(errorKey);
    } catch (IllegalArgumentException ignored) {
      return ErrorCode.INVALID_REQUEST;
    }
  }

  private ResponseEntity<ApiResponse<Void>> buildErrorResponse(HttpStatus status, int code, String message) {
    ApiResponse<Void> response = new ApiResponse<>();
    response.setCode(code);
    response.setMessage(message);
    return ResponseEntity.status(status).body(response);
  }
}

