package org.wildflowergardening.backend.api.kernel.application.exception;

/*
 403 응답코드 내려줄 상황
 */
public class ForbiddenException extends RuntimeException {

  public ForbiddenException(String message) {
    super(message);
  }
}
