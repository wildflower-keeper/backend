package org.wildflowergardening.backend.api.wildflowergardening.application.auth.user;

import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

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
