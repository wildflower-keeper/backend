package org.wildflowergardening.backend.core.wildflowergardening.domain.auth;

import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ShelterPublic;

@Getter
@Builder
public class ShelterPublicUserContext implements UserContext {

  private Long shelterPublicId;
  private Long shelterId;
  private String shelterName;
  private String deviceId;

  public static ShelterPublicUserContext from(ShelterPublic shelterPublic) {
    return ShelterPublicUserContext.builder()
        .shelterPublicId(shelterPublic.getId())
        .shelterId(shelterPublic.getShelter().getId())
        .shelterName(shelterPublic.getShelter().getName())
        .deviceId(shelterPublic.getDeviceId())
        .build();
  }

  @Override
  public UserRole getUserRole() {
    return UserRole.SHELTER_PUBLIC;
  }

  @Override
  public Long getUserId() {
    return this.shelterPublicId;
  }

  @Override
  public String getUsername() {
    return this.shelterName;
  }
}
