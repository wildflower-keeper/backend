package org.wildflowergardening.backend.api.wildflowergardening.application;

import static org.wildflowergardening.backend.api.wildflowergardening.application.exception.WildflowerExceptionType.HOMELESS_APP_CREATE_ACCOUNT_SHELTER_ID_PIN_INVALID;
import static org.wildflowergardening.backend.api.wildflowergardening.application.exception.WildflowerExceptionType.HOMELESS_APP_ESSENTIAL_TERMS_NOT_AGREED;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.api.kernel.application.exception.ApplicationLogicException;
import org.wildflowergardening.backend.api.kernel.application.exception.ForbiddenException;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.HomelessAppJwtProvider;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.HomelessUserContext;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateHomelessRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessAppMainResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessAppMainResponse.UpcomingSleepover;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessAppMainResponse.UpcomingSleepover.UpcomingSleepoverStatus;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessSleepoverResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessTermsResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessTokenRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessTokenResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.UpdateLocationRequest;
import org.wildflowergardening.backend.api.wildflowergardening.util.PhoneNumberFormatter;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessCommandService;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessQueryService;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessTermsAgreeService;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessTermsService;
import org.wildflowergardening.backend.core.wildflowergardening.application.LocationTrackingService;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterPinService;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.application.UnregisteredSleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.CreateSleepoverDto;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTracking;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Sleepover;
import org.wildflowergardening.backend.core.wildflowergardening.domain.UnregisteredSleepover;

@Service
@RequiredArgsConstructor
public class HomelessAppService {

  private final ShelterPinService shelterPinService;
  private final HomelessTermsService homelessTermsService;
  private final HomelessTermsAgreeService homelessTermsAgreeService;
  private final ShelterService shelterService;
  private final HomelessCommandService homelessCommandService;
  private final HomelessQueryService homelessQueryService;
  private final SleepoverService sleepoverService;
  private final HomelessAppJwtProvider homelessAppJwtProvider;
  private final LocationTrackingService locationTrackingService;
  private final UnregisteredSleepoverService unregisteredSleepoverService;

  public List<HomelessTermsResponse> getAllTerms() {
    return homelessTermsService.findAll(LocalDate.now()).stream()
        .map(homelessTerms -> HomelessTermsResponse.builder()
            .id(homelessTerms.getId())
            .title(homelessTerms.getTitle())
            .detail(homelessTerms.getDetail())
            .isEssential(homelessTerms.getIsEssential())
            .build())
        .toList();
  }

  public HomelessTokenResponse createHomeless(CreateHomelessRequest request) {
    Optional<Shelter> shelterOptional = shelterService.getShelterById(request.getShelterId());

    if (shelterOptional.isEmpty() ||
        !shelterPinService.matches(shelterOptional.get().getId(), request.getShelterPin())
    ) {
      throw new ApplicationLogicException(HOMELESS_APP_CREATE_ACCOUNT_SHELTER_ID_PIN_INVALID);
    }
    Map<Long, Boolean> termsIdAndEssential = homelessTermsService.findAllIdEssential(
        LocalDate.now()
    );
    Set<Long> allTermsId = termsIdAndEssential.keySet();
    List<Long> essentialTermsIds = allTermsId.stream()
        .filter(termsId -> termsIdAndEssential.get(termsId).equals(Boolean.TRUE))
        .toList();
    HashSet<Long> termsIdsToAgree = request.getTermsIdsToAgree();

    // 약관동의 요청 validation
    for (Long termsIdToAgree : termsIdsToAgree) {
      if (!allTermsId.contains(termsIdToAgree)) {
        throw new IllegalArgumentException("존재하지 않는 약관에 동의하였습니다.");
      }
    }

    // 필수 약관에 모두 동의했는지 검사
    for (Long essentialId : essentialTermsIds) {
      if (!termsIdsToAgree.contains(essentialId)) {
        throw new ApplicationLogicException(HOMELESS_APP_ESSENTIAL_TERMS_NOT_AGREED);
      }
    }

    // 계정 생성
    Long homelessId = homelessCommandService.create(Homeless.builder()
        .name(request.getName())
        .shelter(shelterOptional.get())
        .deviceId(request.getDeviceId())
        .room(request.getRoom())
        .birthDate(request.getBirthDate())
        .phoneNumber(PhoneNumberFormatter.format(request.getPhoneNumber()))
        .admissionDate(request.getAdmissionDate())
        .build());

    String token = homelessAppJwtProvider.createToken(HomelessUserContext.builder()
        .homelessId(homelessId)
        .homelessName(request.getName())
        .shelterId(request.getShelterId())
        .build());

    return HomelessTokenResponse.builder()
        .homelessId(homelessId)
        .accessToken(token)
        .build();
  }

  @Transactional
  public HomelessTokenResponse getToken(HomelessTokenRequest request) {
    Homeless homeless = homelessQueryService.getOneById(request.getHomelessId())
        .orElseThrow(() -> new ForbiddenException(""));

    if (homeless.getShelter().getId().equals(request.getShelterId())
        && shelterPinService.matches(request.getShelterId(), request.getShelterPin())
        && homeless.getName().equals(request.getHomelessName())
    ) {
      String requestDeviceId = request.getDeviceId();
      String requestPhoneNumber = PhoneNumberFormatter.format(request.getPhoneNumber());

      if (!StringUtils.isEmpty(requestDeviceId) && requestDeviceId.equals(homeless.getDeviceId())) {
        String token = homelessAppJwtProvider.createToken(HomelessUserContext.builder()
            .homelessId(homeless.getId())
            .homelessName(homeless.getName())
            .shelterId(homeless.getShelter().getId())
            .build());
        return HomelessTokenResponse.builder()
            .homelessId(homeless.getId())
            .accessToken(token)
            .build();
      }
      if (!StringUtils.isEmpty(requestPhoneNumber) && requestPhoneNumber.equals(
          homeless.getPhoneNumber())) {
        homeless.setDeviceId(request.getDeviceId());
        String token = homelessAppJwtProvider.createToken(HomelessUserContext.builder()
            .homelessId(homeless.getId())
            .homelessName(homeless.getName())
            .shelterId(homeless.getShelter().getId())
            .build());
        return HomelessTokenResponse.builder()
            .homelessId(homeless.getId())
            .accessToken(token)
            .build();
      }
    }
    throw new ForbiddenException("");
  }

  public HomelessAppMainResponse getHomelessMainInfo(Long homelessId) {
    Homeless homeless = homelessQueryService.getOneById(homelessId)
        .orElseThrow(() -> new IllegalArgumentException("노숙인 정보가 존재하지 않습니다."));

    LocalDateTime nowDateTime = LocalDateTime.now();
    LocalDate nowDate = nowDateTime.toLocalTime().isBefore(LocalTime.of(6, 0))
        ? nowDateTime.toLocalDate().minusDays(1)
        : nowDateTime.toLocalDate();

    Optional<UpcomingSleepover> upcomingSleepoverOptional =
        sleepoverService.getUpcomingSleepover(homelessId, nowDate).stream()
            .map(sleepover -> {
              UpcomingSleepoverStatus status;

              if (sleepover.getStartDate().isBefore(nowDate)
                  || sleepover.getStartDate().isEqual(nowDate)) {
                if (nowDateTime.toLocalTime().isBefore(LocalTime.of(6, 0))
                    || nowDateTime.toLocalTime().isAfter(LocalTime.of(23, 0))) {
                  status = UpcomingSleepoverStatus.IN_PROGRESS;
                } else {
                  status = UpcomingSleepoverStatus.TODAY_SCHEDULED;
                }
              } else {
                status = UpcomingSleepoverStatus.FUTURE_SCHEDULED;
              }
              return UpcomingSleepover.builder()
                  .sleepoverId(sleepover.getId())
                  .nightCount(Period.between(sleepover.getStartDate(), sleepover.getEndDate())
                      .getDays())
                  .startDate(sleepover.getStartDate())
                  .endDate(sleepover.getEndDate())
                  .status(status)
                  .build();
            })
            .findAny();

    return HomelessAppMainResponse.builder()
        .id(homeless.getId())
        .homelessName(homeless.getName())
        .shelterId(homeless.getShelter().getId())
        .shelterName(homeless.getShelter().getName())
        .upcomingSleepover(upcomingSleepoverOptional.orElse(null))
        .build();
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
      if (exclusiveDates.contains(date)) {
        continue;
      }
      resultDates.add(date);
    }
    return resultDates;
  }

  public List<HomelessSleepoverResponse> getSleepoversEndDateAfterToday(Long homelessId) {
    LocalDate now = LocalDate.now();
    return sleepoverService.getAllSleepoversEndDateAfter(homelessId, LocalDate.now()).stream()
        .map(sleepover -> HomelessSleepoverResponse.builder()
            .sleepoverId(sleepover.getId())
            .startDate(sleepover.getStartDate())
            .endDate(sleepover.getEndDate())
            .reason(sleepover.getReason())
            .isCancelable(sleepover.cancelableAt(now))
            .build())
        .toList();
  }

  /**
   * @param dtoList lastTrackedAt ASC 정렬된 리스트
   */
  public void updateLocationStatus(
      Long homelessId, Long shelterId, List<UpdateLocationRequest> dtoList
  ) {
    if (dtoList.isEmpty()) {
      return;
    }
    // 위치 확인 일시 유효성검사
    LocalDateTime beforeLast = LocalDateTime.MIN;
    for (UpdateLocationRequest dto : dtoList) {
      if (dto.getFirstTrackedAt().isAfter(dto.getLastTrackedAt())) {
        throw new IllegalArgumentException("firstTrackedAt 이 lastTrackedAt 보다 큰 위치 정보가 있습니다.");
      }
      if (dto.getFirstTrackedAt().isBefore(beforeLast)) {
        throw new IllegalArgumentException("요청 목록에 위치 확인 기간 중복이 있습니다.");
      }
      beforeLast = dto.getLastTrackedAt();
    }

    // List<LocationTracking> 만들기
    List<LocationTracking> newLocations = new ArrayList<>();

    for (UpdateLocationRequest dto : dtoList) {
      if (newLocations.isEmpty()) {
        newLocations.add(LocationTracking.builder()
            .homelessId(homelessId)
            .shelterId(shelterId)
            .locationStatus(dto.getLocationStatus())
            .lastLatitude(dto.getLastLatitude())
            .lastLongitude(dto.getLastLongitude())
            .firstTrackedAt(dto.getFirstTrackedAt())
            .lastTrackedAt(dto.getLastTrackedAt())
            .build());
        continue;
      }
      LocationTracking last = newLocations.get(newLocations.size() - 1);

      if (last.getLocationStatus() == dto.getLocationStatus()) {
        last.setLastTrackedAt(dto.getLastTrackedAt());
        last.setLastLatitude(dto.getLastLatitude());
        last.setLastLongitude(dto.getLastLongitude());
        continue;
      }
      newLocations.add(LocationTracking.builder()
          .homelessId(homelessId)
          .shelterId(shelterId)
          .locationStatus(dto.getLocationStatus())
          .lastLatitude(dto.getLastLatitude())
          .lastLongitude(dto.getLastLongitude())
          .firstTrackedAt(dto.getFirstTrackedAt())
          .lastTrackedAt(dto.getLastTrackedAt())
          .build());
    }
    locationTrackingService.createOrUpdate(newLocations);

    // 23시 - 24시 외출중 ; lastTrackedAt 에 대해 오름차순 정렬되어있음
    List<LocationTracking> nightOuting1 = newLocations.stream()
        .filter(newLocation -> newLocation.getLocationStatus() == LocationStatus.OUTING
            && newLocation.getLastTrackedAt().toLocalTime().isAfter(LocalTime.of(23, 0)))
        .toList();
    // 0시 - 5시 외출중 ; lastTrackedAt 에 대해 오름차순 정렬되어있음
    List<LocationTracking> nightOuting2 = newLocations.stream()
        .filter(newLocation -> newLocation.getLocationStatus() == LocationStatus.OUTING
            && newLocation.getLastTrackedAt().toLocalTime().isBefore(LocalTime.of(5, 0)))
        .toList();

    /*
     외박신청 안한 날 야간 외출 감지 -> 미신청 외박 데이터 생성
     */
    if (nightOuting1.isEmpty() && nightOuting2.isEmpty()) {
      return;
    }
    Homeless homeless = homelessQueryService.getOneById(homelessId)
        .orElseThrow(() -> new IllegalArgumentException("homeless not found"));

    UnregisteredSleepover unregisteredSleepover = null;

    if (!nightOuting1.isEmpty()) {
      LocationTracking lastLocationTracking = nightOuting1.get(nightOuting1.size() - 1);
      LocalDate targetDate = lastLocationTracking.getLastTrackedAt().toLocalDate();

      if (!sleepoverService.exist(homelessId, targetDate)) {
        // 23시 - 24시에 무단외박 감지됨
        unregisteredSleepover = UnregisteredSleepover.builder()
            .homeless(homeless)
            .shelterId(shelterId)
            .targetDate(targetDate)
            .firstTrackedAt(lastLocationTracking.getLastTrackedAt())
            .lastTrackedAt(lastLocationTracking.getLastTrackedAt())
            .build();
      }
    }
    if (!nightOuting2.isEmpty()) {
      LocationTracking lastLocationTracking = nightOuting2.get(nightOuting2.size() - 1);
      LocalDate targetDate = lastLocationTracking.getLastTrackedAt().toLocalDate().minusDays(1);

      if (!sleepoverService.exist(homelessId, targetDate)) {
        // 0시 - 5시에 무단외박 감지됨
        if (unregisteredSleepover != null) {
          unregisteredSleepover.setLastTrackedAt(lastLocationTracking.getLastTrackedAt());
        } else {
          unregisteredSleepover = UnregisteredSleepover.builder()
              .homeless(homeless)
              .shelterId(shelterId)
              .targetDate(targetDate)
              .firstTrackedAt(lastLocationTracking.getLastTrackedAt())
              .lastTrackedAt(lastLocationTracking.getLastTrackedAt())
              .build();
        }
      }
    }
    unregisteredSleepoverService.createOrUpdate(unregisteredSleepover);
  }

  public boolean isSleepoverTonight(Long homelessId) {
    return !sleepoverService.filterSleepoverHomelessIds(List.of(homelessId), LocalDate.now())
        .isEmpty();
  }

  public void deleteSleepover(Long homelessId, Long sleepoverId) {
    sleepoverService.delete(homelessId, sleepoverId);
  }
}
