package org.wildflowergardening.backend.api.wildflowergardening.application;

import static org.wildflowergardening.backend.api.wildflowergardening.application.exception.WildflowerExceptionType.SHELTER_ADMIN_LOGIN_ID_PASSWORD_INVALID;

import io.micrometer.common.util.StringUtils;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.api.kernel.application.exception.ApplicationLogicException;
import org.wildflowergardening.backend.api.kernel.application.exception.ForbiddenException;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessCountResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessPageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse.PageInfoResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.SessionResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterAdminSleepoverResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterLoginRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterPinResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.UpdateHomelessRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.pager.HomelessFilterPagerProvider;
import org.wildflowergardening.backend.api.wildflowergardening.util.PhoneNumberFormatter;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessCommandService;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessQueryService;
import org.wildflowergardening.backend.core.wildflowergardening.application.LocationTrackingService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SessionService;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterPinService;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTracking;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ShelterPin;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Sleepover;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@Service
@RequiredArgsConstructor
public class ShelterAdminAppService {

  private final SessionService sessionService;
  private final ShelterService shelterService;
  private final PasswordEncoder passwordEncoder;
  private final ShelterPinService shelterPinService;
  private final HomelessFilterPagerProvider homelessFilterPagerProvider;
  private final SleepoverService sleepoverService;
  private final HomelessQueryService homelessQueryService;
  private final LocationTrackingService locationTrackingService;
  private final HomelessCommandService homelessCommandService;

  public SessionResponse login(ShelterLoginRequest dto) {
    Optional<Shelter> shelterOptional = shelterService.getShelterById(dto.getId());

    if (shelterOptional.isEmpty() || !passwordEncoder.matches(
        dto.getPw(), shelterOptional.get().getPassword()
    )) {
      throw new ApplicationLogicException(SHELTER_ADMIN_LOGIN_ID_PASSWORD_INVALID);
    }

    Shelter shelter = shelterOptional.get();
    LocalDateTime now = LocalDateTime.now();

    byte[] randomBytes = new byte[80];
    new SecureRandom().nextBytes(randomBytes);
    Session session = Session.builder()
        .token(Base64.getUrlEncoder().encodeToString(randomBytes).substring(0, 80))
        .userRole(UserRole.SHELTER)
        .userId(shelter.getId())
        .username(shelter.getName())
        .createdAt(now)
        .expiredAt(now.plusHours(1))
        .build();

    session = sessionService.save(session);

    return SessionResponse.builder()
        .authToken(session.getToken())
        .expiredAt(session.getExpiredAt())
        .build();
  }

  public ShelterPinResponse getPin(Long shelterId) {
    ShelterPin shelterPin = shelterPinService.getShelterPin(shelterId);
    return ShelterPinResponse.builder()
        .pin(shelterPin.getPin())
        .expiredAt(shelterPin.calcExpiredAt())
        .build();
  }

  public NumberPageResponse<HomelessResponse> getHomelessPage(HomelessPageRequest pageRequest) {
    return homelessFilterPagerProvider.from(pageRequest.getFilterType())
        .getPage(pageRequest);
  }

  public HomelessCountResponse countHomeless(Long shelterId, LocalDateTime targetDateTime) {
    long totalHomelessCount = homelessQueryService.count(shelterId);

    // 외박
    LocalDate sleepoverTargetDate = targetDateTime.toLocalDate();
    long sleepoverCount = sleepoverService.count(shelterId, sleepoverTargetDate);

    // 외출
    LocalDateTime lastLocationTrackedAfter = targetDateTime.minusHours(1);
    Map<Long, LocationTracking> lastLocationMap =        // map key : Homeless id
        locationTrackingService.getAllLastByLastTrackedAfter(
            shelterId, lastLocationTrackedAfter
        );
    long trackedCount = lastLocationMap.size();
    long inShelterCount = lastLocationMap.values().stream()
        .filter(locationTracking ->
            locationTracking.getLocationStatus() == LocationStatus.IN_SHELTER)
        .count();
    long outingCount = lastLocationMap.values().stream()
        .filter(locationTracking ->
            locationTracking.getLocationStatus() == LocationStatus.OUTING)
        .count();

    return HomelessCountResponse.builder()
        .totalHomelessCount(totalHomelessCount)
        .sleepoverTargetDate(sleepoverTargetDate)
        .sleepoverCount(sleepoverCount)
        .locationTrackedHomelessCount(trackedCount)
        .locationTrackedAfter(lastLocationTrackedAfter)
        .outingCount(outingCount)
        .inShelterCount(inShelterCount)
        .build();
  }

  public NumberPageResponse<ShelterAdminSleepoverResponse> getPage(
      Long shelterId, int pageNumber, int pageSize
  ) {
    NumberPageResult<Sleepover> result = sleepoverService.getPage(shelterId, pageNumber, pageSize);

    return NumberPageResponse.<ShelterAdminSleepoverResponse>builder()
        .items(result.getItems().stream()
            .map(sleepover -> ShelterAdminSleepoverResponse.builder()
                .sleepoverId(sleepover.getId())
                .homelessId(sleepover.getHomelessId())
                .homelessName(sleepover.getHomelessName())
                .homelessPhoneNumber(sleepover.getHomelessPhoneNumber())
                .startDate(sleepover.getStartDate())
                .endDate(sleepover.getEndDate())
                .createdAt(sleepover.getCreatedAt())
                .build())
            .collect(Collectors.toList()))
        .pagination(PageInfoResponse.of(result.getPagination()))
        .build();
  }

  @Transactional
  public void updateHomelessInfo(
      Long shelterId, Long homelessId, UpdateHomelessRequest request
  ) {
    Homeless homeless = homelessQueryService.getOneById(homelessId)
        .orElseThrow(() -> new IllegalArgumentException("not exist homeless"));

    if (!homeless.getShelter().getId().equals(shelterId)) {
      throw new ForbiddenException("");
    }
    homeless.setName(
        StringUtils.isBlank(request.getName())
            ? homeless.getName()
            : request.getName()
    );
    homeless.setRoom(
        StringUtils.isBlank(request.getRoom())
            ? homeless.getRoom()
            : request.getRoom()
    );
    homeless.setPhoneNumber(
        StringUtils.isBlank(request.getPhoneNumber())
            ? homeless.getPhoneNumber()
            : PhoneNumberFormatter.format(request.getPhoneNumber())
    );
    homeless.setBirthDate(
        request.getBirthDate() == null
            ? homeless.getBirthDate()
            : request.getBirthDate()
    );
    homeless.setAdmissionDate(
        request.getAdmissionDate() == null
            ? homeless.getAdmissionDate()
            : request.getAdmissionDate()
    );
  }

  public void deleteHomeless(Long homelessId, Long shelterId) {
    homelessCommandService.deleteHomeless(homelessId, shelterId);
  }
}
