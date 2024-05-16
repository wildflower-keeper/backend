package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SessionResponse {

  private String sessionId;
  private LocalDateTime expiredAt;
}
