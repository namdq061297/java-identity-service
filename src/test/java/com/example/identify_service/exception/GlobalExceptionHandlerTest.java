package com.example.identify_service.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.identify_service.dto.request.ApiResponse;
import com.example.identify_service.dto.request.UserCreationRequest;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {

  @Test
  void handleAppExceptionReturnsMappedResponse() {
    GlobalExceptionHandler handler = new GlobalExceptionHandler();

    ResponseEntity<ApiResponse<Void>> response =
        handler.handleAppException(new AppException(ErrorCode.USER_EXISTED));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getHttpStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    assertThat(response.getBody().getCode()).isEqualTo(1002);
    assertThat(response.getBody().getMessage()).isEqualTo(ErrorCode.USER_EXISTED.getMessage());
    assertThat(response.getBody().getResult()).isNull();
  }

  @Test
  void handleMalformedBodyReturnsBadRequestResponse() {
    GlobalExceptionHandler handler = new GlobalExceptionHandler();

    ResponseEntity<ApiResponse<Void>> response =
        handler.handleMalformedBody(new HttpMessageNotReadableException(
            "Malformed JSON",
            new MockHttpInputMessage(new byte[0])
        ));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.INVALID_REQUEST.getCode());
    assertThat(response.getBody().getMessage()).isEqualTo(ErrorCode.INVALID_REQUEST.getMessage());
    assertThat(response.getBody().getResult()).isNull();
  }

  @Test
  void handleValidationErrorReturnsBadRequestResponse() throws Exception {
    GlobalExceptionHandler handler = new GlobalExceptionHandler();

    Method method = GlobalExceptionHandlerTest.class.getDeclaredMethod("validationTarget", UserCreationRequest.class);
    MethodParameter parameter = new MethodParameter(method, 0);
    BindingResult bindingResult = new BeanPropertyBindingResult(new UserCreationRequest(), "userCreationRequest");
    bindingResult.rejectValue("password", "Size", "INVALID_PASSWORD");
    MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

    ResponseEntity<ApiResponse<Void>> response = handler.handleValidationError(exception);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.INVALID_PASSWORD.getCode());
    assertThat(response.getBody().getMessage()).isEqualTo(ErrorCode.INVALID_PASSWORD.getMessage());
    assertThat(response.getBody().getResult()).isNull();
  }

  @Test
  void handleValidationErrorReturnsRequiredFieldMessageWhenFieldMissing() throws Exception {
    GlobalExceptionHandler handler = new GlobalExceptionHandler();

    Method method = GlobalExceptionHandlerTest.class.getDeclaredMethod("validationTarget", UserCreationRequest.class);
    MethodParameter parameter = new MethodParameter(method, 0);
    BindingResult bindingResult = new BeanPropertyBindingResult(new UserCreationRequest(), "userCreationRequest");
    bindingResult.rejectValue("firstName", "NotBlank", "FIRST_NAME_REQUIRED");
    MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

    ResponseEntity<ApiResponse<Void>> response = handler.handleValidationError(exception);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.FIRST_NAME_REQUIRED.getCode());
    assertThat(response.getBody().getMessage()).isEqualTo(ErrorCode.FIRST_NAME_REQUIRED.getMessage());
    assertThat(response.getBody().getResult()).isNull();
  }

  @SuppressWarnings("unused")
  private void validationTarget(UserCreationRequest request) {
    // helper method used to create a MethodParameter for validation exception tests
  }
}
