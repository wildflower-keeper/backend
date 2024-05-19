package org.wildflowergardening.backend.api.kernel.application.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {

  private String errorCode;
  private String description;
}
