package org.wildflowergardening.backend.core.wildflowergardening.domain.auth;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;

@Getter
@Builder
public class HomelessUserContext implements UserContext {

  private Long homelessId;
  private String homelessName;
  private Long shelterId;
  private String phoneNumber;
  private LocalDate birthDate;
  private String room;

  public static HomelessUserContext from(Homeless homeless) {
    return HomelessUserContext.builder()
        .homelessId(homeless.getId())
        .homelessName(homeless.getName())
        .shelterId(homeless.getShelterId())
        .phoneNumber(homeless.getPhoneNumber())
        .birthDate(homeless.getBirthDate())
        .room(homeless.getRoom())
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
