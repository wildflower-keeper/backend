package org.wildflowergardening.backend.core.wildflowergardening.application.dto;

import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.UserRole;

@Getter
@Builder
public class UserContext {

  private UserRole userRole;
  private Long userId;
  private String username;
}
