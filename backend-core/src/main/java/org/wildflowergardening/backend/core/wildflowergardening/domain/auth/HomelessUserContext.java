package org.wildflowergardening.backend.core.wildflowergardening.domain.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomelessUserContext implements UserContext {

  private Long homelessId;
  private String homelessName;
  private Long shelterId;

  @Override
  public UserRole getUserRole() {
    return UserRole.HOMELESS;
  }

  @Override
  public Long getUserId() {
    return this.homelessId;
  }

  @Override
  public String getUsername() {
    return this.homelessName;
  }
}
