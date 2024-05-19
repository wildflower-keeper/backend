package org.wildflowergardening.backend.api.wildflowergardening.application;

import static org.wildflowergardening.backend.api.wildflowergardening.application.exception.WildflowerExceptionType.SHELTER_LOGIN_ID_PASSWORD_INVALID;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.api.kernel.application.exception.ApplicationLogicException;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateHomelessRequest;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessService;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterService;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;

@Service
@RequiredArgsConstructor
public class HomelessManagingService {

  private final PasswordEncoder passwordEncoder;
  private final ShelterService shelterService;
  private final HomelessService homelessService;

  public Long createHomeless(CreateHomelessRequest request) {
    Optional<Shelter> shelterOptional = shelterService.getShelterById(request.getShelterId());

    if (shelterOptional.isEmpty() || !passwordEncoder.matches(
        request.getShelterPw(), shelterOptional.get().getPassword()
    )) {
      throw new ApplicationLogicException(SHELTER_LOGIN_ID_PASSWORD_INVALID);
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
