package org.wildflowergardening.backend.api.wildflowergardening.application.auth.user;

import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@Getter
@Builder
public class HomelessUserContext implements UserContext {

  private Long homelessId;
  private String homelessName;
  private Long shelterId;

  public static HomelessUserContext from(Homeless homeless) {
    return HomelessUserContext.builder()
        .homelessId(homeless.getId())
        .homelessName(homeless.getName())
        .shelterId(homeless.getShelter().getId())
        .build();
  }

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
