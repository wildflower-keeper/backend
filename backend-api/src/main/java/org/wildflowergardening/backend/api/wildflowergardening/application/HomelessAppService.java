package org.wildflowergardening.backend.api.wildflowergardening.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateHomelessRequest;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessService;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.ShelterIdPasswordDto;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;

@Service
@RequiredArgsConstructor
public class HomelessAppService {

  private final ShelterService shelterService;
  private final HomelessService homelessService;

  public Long createHomeless(CreateHomelessRequest request) {
    Optional<Shelter> shelterOptional = shelterService.getShelterByAuthInfo(
        ShelterIdPasswordDto.builder()
            .shelterId(request.getShelterId())
            .password(request.getShelterPw())
            .build());

    if (shelterOptional.isEmpty()) {
      throw new IllegalArgumentException("계정 생성 권한 없음");
    }
    return homelessService.create(Homeless.builder()
        .name(request.getName())
        .shelterId(request.getShelterId())
        .deviceId(request.getDeviceId())
        .room(request.getRoom())
        .birthDate(request.getBirthDate())
        .phoneNumber(request.getPhoneNumber())
        .admissionDate(request.getAdmissionDate())
        .build());
  }
}
