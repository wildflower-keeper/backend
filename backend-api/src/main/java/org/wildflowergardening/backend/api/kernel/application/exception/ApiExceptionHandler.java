package org.wildflowergardening.backend.api.kernel.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.wildflowergardening.backend.core.kernel.application.exception.ApplicationLogicException;
import org.wildflowergardening.backend.core.kernel.application.exception.CustomException;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler({ApplicationLogicException.class})
  public ResponseEntity<ErrorResponse> handleApplicationLogicException(
      CustomException e
  ) {
    return ResponseEntity.ok(ErrorResponse.builder()
        .errorCode(e.getExceptionType().code())
        .description(e.getExceptionType().message())
        .build());
  }

  @ExceptionHandler({ForbiddenException.class})
  public ResponseEntity<String> handleForbiddenException(
      ForbiddenException e
  ) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(e.getMessage());
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<String> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e
  ) {
    FieldError fieldError = e.getBindingResult().getFieldError();

    return ResponseEntity.badRequest()
        .body(fieldError != null ? fieldError.getDefaultMessage() : null);
  }

  @ExceptionHandler({IllegalArgumentException.class})
  public ResponseEntity<String> handleIllegalArgumentException(
      IllegalArgumentException e
  ) {
    return ResponseEntity.badRequest()
        .body(e.getMessage());
  }
}
