package org.wildflowergardening.backend.core.wildflowergardening.domain.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShelterUserContext implements UserContext {

  private Long shelterId;
  private String shelterName;

  @Override
  public UserRole getUserRole() {
    return UserRole.SHELTER;
  }

  @Override
  public Long getUserId() {
    return this.shelterId;
  }

  @Override
  public String getUsername() {
    return this.shelterName;
  }
}
