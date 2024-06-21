package org.wildflowergardening.backend.core.kernel.application.exception;

import lombok.Getter;

/*
 클라이언트에서 별도로 처리해줄 정상적인 에러 상황
 */
@Getter
public class ApplicationLogicException extends RuntimeException implements CustomException {

  private final ExceptionType exceptionType;

  public ApplicationLogicException(ExceptionType exceptionType) {
    this.exceptionType = exceptionType;
  }
}
