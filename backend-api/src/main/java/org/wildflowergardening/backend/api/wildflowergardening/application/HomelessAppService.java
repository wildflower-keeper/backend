package org.wildflowergardening.backend.api.wildflowergardening.application;

import static org.wildflowergardening.backend.api.wildflowergardening.application.exception.WildflowerExceptionType.SHELTER_ADMIN_LOGIN_ID_PASSWORD_INVALID;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.api.kernel.application.exception.ApplicationLogicException;
import org.wildflowergardening.backend.api.kernel.application.exception.ForbiddenException;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateHomelessRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateSleepoverRequest;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessService;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless.LocationStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Sleepover;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.HomelessUserContext;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@Service
@RequiredArgsConstructor
public class HomelessAppService {

  private final PasswordEncoder passwordEncoder;
  private final ShelterService shelterService;
  private final HomelessService homelessService;
  private final SleepoverService sleepoverService;

  public Long createHomeless(CreateHomelessRequest request) {
    Optional<Shelter> shelterOptional = shelterService.getShelterById(request.getShelterId());

    if (shelterOptional.isEmpty() || !passwordEncoder.matches(
        request.getShelterPw(), shelterOptional.get().getPassword()
    )) {
      throw new ApplicationLogicException(SHELTER_ADMIN_LOGIN_ID_PASSWORD_INVALID);
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

  @Transactional
  public Long createSleepover(
      HomelessUserContext homeless, CreateSleepoverRequest request
  ) {
    if (!homeless.getHomelessId().equals(request.getHomelessId())) {
      throw new ForbiddenException("인가된 사용자와 외박 신청자 id가 맞지 않습니다.");
    }
    return sleepoverService.create(Sleepover.builder()
        .creatorType(UserRole.HOMELESS)
        .homelessId(homeless.getHomelessId())
        .homelessName(homeless.getHomelessName())
        .phoneNumber(homeless.getPhoneNumber())
        .shelterId(homeless.getShelterId())
        .startDate(request.getStartDate())
        .endDate(request.getEndDate())
        .build());
  }

  public void updateLocationStatus(
      Long homelessId, LocationStatus lastLocationStatus, LocalDateTime lastLocationTrackedAt
  ) {
    homelessService.updateLocationStatus(homelessId, lastLocationStatus, lastLocationTrackedAt);
  }

  @Transactional(readOnly = true)
  public boolean isSleepoverTonight(Long homelessId) {
    return !sleepoverService.filterSleepoverHomelessIds(List.of(homelessId), LocalDate.now())
        .isEmpty();
  }
}
