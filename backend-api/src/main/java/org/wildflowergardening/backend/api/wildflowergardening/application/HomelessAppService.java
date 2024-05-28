package org.wildflowergardening.backend.api.wildflowergardening.application;

import static org.wildflowergardening.backend.api.wildflowergardening.application.exception.WildflowerExceptionType.SHELTER_ADMIN_LOGIN_ID_PASSWORD_INVALID;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.api.kernel.application.exception.ApplicationLogicException;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.HomelessAppJwtProvider;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.HomelessUserContext;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateHomelessRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateHomelessResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessAppSleepoverResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessMainResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessMainResponse.HomelessMainResponseBuilder;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessCommandService;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessQueryService;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.CreateSleepoverDto;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless.LocationStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Sleepover;

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

  public HomelessMainResponse getHomelessMainInfo(Long homelessId, LocalDate targetDate) {
    Homeless homeless = homelessQueryService.getOneById(homelessId)
        .orElseThrow(() -> new IllegalArgumentException("노숙인 정보가 존재하지 않습니다."));

    Optional<Sleepover> sleepoverOptional = sleepoverService.getFirstAfter(homelessId, targetDate);

    HomelessMainResponseBuilder builder = HomelessMainResponse.builder()
        .id(homeless.getId())
        .name(homeless.getName())
        .shelterName(homeless.getShelter().getName());

    if (sleepoverOptional.isPresent()) {
      Sleepover sleepover = sleepoverOptional.get();
      builder.planedSleepover(HomelessAppSleepoverResponse.builder()
              .sleepoverId(sleepover.getId())
              .startDate(sleepover.getStartDate())
              .endDate(sleepover.getEndDate())
              .build());
    }
    return builder.build();
  }

  public Long createSleepover(CreateSleepoverDto dto) {
    if (!dto.getEndDate().isAfter(dto.getStartDate())) {
      throw new IllegalArgumentException("외박신청 종료일은 시작일의 다음날 이후여야 합니다.");
    }
    // 외박 기간 허용 범위 검사
    // Sleepover.calcMinStartDate(now) 이상, Sleepover.calcMaxEndDate(now) 이하 기간동안 외박 신청 가능
    LocalDate now = LocalDate.now();
    if (dto.getStartDate().isBefore(Sleepover.calcMinStartDate(now))
        || dto.getEndDate().isAfter(Sleepover.calcMaxEndDate(now))) {
      throw new IllegalArgumentException("외박 신청 가능한 일자 범위를 벗어났습니다.");
    }
    return sleepoverService.create(dto);
  }

  public List<LocalDate> getAvailableSleepoverDates(Long homelessId) {
    LocalDate now = LocalDate.now();

    LocalDate periodStart = Sleepover.calcMinStartDate(now).plusDays(1);
    LocalDate periodEnd = Sleepover.calcMaxEndDate(now).minusDays(1);

    Set<LocalDate> exclusiveDates = new HashSet<>();

    List<Sleepover> alreadyExistSleepovers = sleepoverService.getSleepoversForPeriod(
        homelessId, periodStart, periodEnd
    );
    for (Sleepover sleepover : alreadyExistSleepovers) {
      for (LocalDate date = sleepover.getStartDate()
          ; !date.isAfter(sleepover.getEndDate())
          ; date = date.plusDays(1)) {
        exclusiveDates.add(date);
      }
    }
    List<LocalDate> resultDates = new ArrayList<>();

    for (LocalDate date = periodStart; !date.isAfter(periodEnd); date = date.plusDays(1)) {
      if (exclusiveDates.contains(date)) continue;
      resultDates.add(date);
    }
    return resultDates;
  }

  public void updateLocationStatus(
      Long homelessId, LocationStatus lastLocationStatus, LocalDateTime lastLocationTrackedAt
  ) {
    homelessCommandService.updateLocationStatus(homelessId, lastLocationStatus,
        lastLocationTrackedAt);
  }

  public boolean isSleepoverTonight(Long homelessId) {
    return !sleepoverService.filterSleepoverHomelessIds(List.of(homelessId), LocalDate.now())
        .isEmpty();
  }

  public void deleteSleepover(Long homelessId, Long sleepoverId) {
    sleepoverService.delete(homelessId, sleepoverId);
  }
}
