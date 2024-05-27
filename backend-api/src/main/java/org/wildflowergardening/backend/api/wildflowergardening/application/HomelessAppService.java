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
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.HomelessAppJwtProvider;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.HomelessUserContext;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateHomelessRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateHomelessResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessMainResponse;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessCommandService;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessQueryService;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.CreateSleepoverDto;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless.LocationStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;

@Service
@RequiredArgsConstructor
public class HomelessAppService {

  private final PasswordEncoder passwordEncoder;
  private final ShelterService shelterService;
  private final HomelessCommandService homelessCommandService;
  private final HomelessQueryService homelessQueryService;
  private final SleepoverService sleepoverService;
  private final HomelessAppJwtProvider homelessAppJwtProvider;

  public CreateHomelessResponse createHomeless(CreateHomelessRequest request) {
    Optional<Shelter> shelterOptional = shelterService.getShelterById(request.getShelterId());

    if (shelterOptional.isEmpty() || !passwordEncoder.matches(
        request.getShelterPw(), shelterOptional.get().getPassword()
    )) {
      throw new ApplicationLogicException(SHELTER_ADMIN_LOGIN_ID_PASSWORD_INVALID);
    }
    Long homelessId = homelessCommandService.create(Homeless.builder()
        .name(request.getName())
        .shelter(shelterOptional.get())
        .deviceId(request.getDeviceId())
        .room(request.getRoom())
        .birthDate(request.getBirthDate())
        .phoneNumber(request.getPhoneNumber())
        .admissionDate(request.getAdmissionDate())
        .build());

    String token = homelessAppJwtProvider.createToken(HomelessUserContext.builder()
        .homelessId(homelessId)
        .homelessName(request.getName())
        .shelterId(request.getShelterId())
        .build());

    return CreateHomelessResponse.builder()
        .homelessId(homelessId)
        .accessToken(token)
        .build();
  }

  public HomelessMainResponse getHomelessById(Long homelessId) {
    return homelessQueryService.getOneById(homelessId)
        .map(homeless -> HomelessMainResponse.builder()
            .id(homeless.getId())
            .name(homeless.getName())
            .shelterName(homeless.getShelter().getName())
            .build())
        .orElseThrow(() -> new IllegalArgumentException("노숙인 정보가 존재하지 않습니다."));
  }

  public Long createSleepover(CreateSleepoverDto dto) {
    if (!dto.getEndDate().isAfter(dto.getStartDate())) {
      throw new IllegalArgumentException("외박신청 종료일은 시작일의 다음날 이후여야 합니다.");
    }
    // 외박 기간 허용 범위 (임시) : 어제 이후 ~ 한달 뒤
    LocalDate now = LocalDate.now();
    if (dto.getStartDate().isBefore(now.minusDays(1))
        || dto.getEndDate().isAfter(now.plusMonths(1))) {
      throw new IllegalArgumentException("외박 신청 가능한 일자 범위를 벗어났습니다.");
    }
    return sleepoverService.create(dto);
  }

  public void updateLocationStatus(
      Long homelessId, LocationStatus lastLocationStatus, LocalDateTime lastLocationTrackedAt
  ) {
    homelessCommandService.updateLocationStatus(homelessId, lastLocationStatus,
        lastLocationTrackedAt);
  }

  @Transactional(readOnly = true)
  public boolean isSleepoverTonight(Long homelessId) {
    return !sleepoverService.filterSleepoverHomelessIds(List.of(homelessId), LocalDate.now())
        .isEmpty();
  }
}
