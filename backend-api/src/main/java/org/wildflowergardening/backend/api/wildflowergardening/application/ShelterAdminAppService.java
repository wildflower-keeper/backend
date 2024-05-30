package org.wildflowergardening.backend.api.wildflowergardening.application;

import static org.wildflowergardening.backend.api.wildflowergardening.application.exception.WildflowerExceptionType.SHELTER_ADMIN_LOGIN_ID_PASSWORD_INVALID;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.api.kernel.application.exception.ApplicationLogicException;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessCountResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessPageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse.PageInfoResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.SessionResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterAdminSleepoverResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterLoginRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.pager.HomelessFilterPagerProvider;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessQueryService;
import org.wildflowergardening.backend.core.wildflowergardening.application.LocationTrackingService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SessionService;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTracking;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Sleepover;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@Service
@RequiredArgsConstructor
public class ShelterAdminAppService {

  private final SessionService sessionService;
  private final ShelterService shelterService;
  private final PasswordEncoder passwordEncoder;
  private final HomelessFilterPagerProvider homelessFilterPagerProvider;
  private final SleepoverService sleepoverService;
  private final HomelessQueryService homelessQueryService;
  private final LocationTrackingService locationTrackingService;

  public SessionResponse login(ShelterLoginRequest dto) {
    Optional<Shelter> shelterOptional = shelterService.getShelterById(dto.getId());

    if (shelterOptional.isEmpty() || !passwordEncoder.matches(
        dto.getPw(), shelterOptional.get().getPassword()
    )) {
      throw new ApplicationLogicException(SHELTER_ADMIN_LOGIN_ID_PASSWORD_INVALID);
    }

    Shelter shelter = shelterOptional.get();
    LocalDateTime now = LocalDateTime.now();

    Session session = sessionService.save(Session.builder()
        .uuid(UUID.randomUUID().toString())
        .userRole(UserRole.SHELTER)
        .userId(shelter.getId())
        .username(shelter.getName())
        .createdAt(now)
        .expiredAt(now.plusHours(1))
        .build());

    return SessionResponse.builder()
        .sessionToken(session.getUuid())
        .expiredAt(session.getExpiredAt())
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
    Map<Long, LocationTracking> lastLocationMap =
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
                .homelessId(sleepover.getHomeless().getId())
                .homelessName(sleepover.getHomeless().getName())
                .startDate(sleepover.getStartDate())
                .endDate(sleepover.getEndDate())
                .createdAt(sleepover.getCreatedAt())
                .build())
            .collect(Collectors.toList()))
        .pagination(PageInfoResponse.of(result.getPagination()))
        .build();
  }
}
