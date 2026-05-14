package com.example.identify_service.exception;

import com.example.identify_service.dto.request.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

  private static final String MIN_ATTRIBUTE = "min";

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
      case UNAUTHENTICATED -> HttpStatus.UNAUTHORIZED;
      default -> HttpStatus.INTERNAL_SERVER_ERROR;
    };
    return buildErrorResponse(status, errorCode.getCode(), errorCode.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
    return buildErrorResponse(HttpStatus.FORBIDDEN, errorCode.getCode(), errorCode.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidationError(MethodArgumentNotValidException ex) {
    ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
    FieldError fieldError = ex.getBindingResult().getFieldError();
    if (fieldError != null && fieldError.getDefaultMessage() != null) {
      errorCode = resolveErrorCode(fieldError.getDefaultMessage());
    }

    Map<String, Object> attributes = getConstraintAttributes(fieldError);
//    String message = buildValidationMessage(errorCode, attributes);
    String message = Objects.nonNull(attributes)
        ? mapAttributesToMessage(errorCode.getMessage(), attributes)
        : errorCode.getMessage();
    log.info("Validation Error: {}", attributes.toString());
    return buildErrorResponse(HttpStatus.BAD_REQUEST, errorCode.getCode(), message);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<Void>> handleMalformedBody(HttpMessageNotReadableException ex) {
    return buildErrorResponse(HttpStatus.BAD_REQUEST,
        ErrorCode.INVALID_REQUEST.getCode(),
        ErrorCode.INVALID_REQUEST.getMessage());
  }

  private ErrorCode resolveErrorCode(String errorKey) {
    try {
      ErrorCode errorCode = ErrorCode.INVALID_MESSAGE_KEY;
      try {
        errorCode = ErrorCode.valueOf(errorKey);
      } catch (IllegalArgumentException e) {
      }
      return errorCode;
    } catch (IllegalArgumentException ignored) {
      return ErrorCode.INVALID_REQUEST;
    }
  }

  private Map<String, Object> getConstraintAttributes(FieldError fieldError) {
    if (fieldError == null) {
      return Map.of();
    }

    try {
      ConstraintViolation<?> constraintViolation = fieldError.unwrap(ConstraintViolation.class);
      return constraintViolation.getConstraintDescriptor().getAttributes();
    } catch (IllegalArgumentException ignored) {
      return Map.of();
    }
  }

  private String buildValidationMessage(ErrorCode errorCode, Map<String, Object> attributes) {
    if (errorCode == ErrorCode.INVALID_DOB) {
      Object minYearOld = attributes.get("minYearOld");
      if (minYearOld != null) {
        return errorCode.getMessage() + minYearOld;
      }
    }

    return errorCode.getMessage();
  }

  private String mapAttributesToMessage(String message, Map<String, Object> attributes) {
    if (attributes == null || attributes.isEmpty()) {
      return message;
    }

    Object minAttributeValue = attributes.get(MIN_ATTRIBUTE);
    if (minAttributeValue != null) {
      String minAttribute = String.valueOf(minAttributeValue);
      return message.replace("{" + MIN_ATTRIBUTE + "}", minAttribute);
    }

    return message;
  }

  private ResponseEntity<ApiResponse<Void>> buildErrorResponse(HttpStatusCode status, int code, String message) {
    ApiResponse<Void> response = new ApiResponse<>();
    response.setHttpStatus(status.value());
    response.setCode(code);
    response.setMessage(message);
    return ResponseEntity.status(status).body(response);
  }
}
