package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTracking;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTrackingRepository;

@Service
@RequiredArgsConstructor
public class LocationTrackingService {

  private final LocationTrackingRepository locationTrackingRepository;

  @Transactional
  public void createOrUpdate(LinkedList<LocationTracking> newLocations) {
    Long homelessId = newLocations.get(0).getHomelessId();

    // 기존에 저장되어있던 마지막 위치 확인 데이터
    Optional<LocationTracking> lastLocationOptional = locationTrackingRepository
        .findTopByHomelessIdOrderByIdDesc(homelessId);

    if (lastLocationOptional.isEmpty()) {
      locationTrackingRepository.saveAll(newLocations);
      return;
    }
    LocationTracking lastLocation = lastLocationOptional.get();
    LocalDateTime lastTrackedAt = lastLocation.getLastTrackedAt();

    LocationTracking newFirstLocation = newLocations.get(0);
    LocalDateTime newFirstTrackedAt = newFirstLocation.getFirstTrackedAt();

    if (lastTrackedAt.isAfter(newFirstTrackedAt)) {
      throw new IllegalArgumentException("기존 위치 정보보다 이전의 위치 정보를 새로 저장할 수 없습니다.");
    }
    if (lastLocation.getLocationStatus() == newFirstLocation.getLocationStatus()
        && lastTrackedAt.isAfter(newFirstTrackedAt.minusHours(1))) {
      lastLocation.setLastTrackedAt(newFirstLocation.getLastTrackedAt());
      lastLocation.setLastLatitude(newFirstLocation.getLastLatitude());
      lastLocation.setLastLongitude(newFirstLocation.getLastLongitude());

      newLocations.remove(0);
    }
    locationTrackingRepository.saveAll(newLocations);
  }

  /**
   * @return map (key : Homeless id, value : 마지막 LocationTracking)
   */
  @Transactional(readOnly = true)
  public Map<Long, LocationTracking> getAllLastByLastTrackedAfter(
      Long shelterId, LocalDateTime lastLocationTrackedAfter
  ) {
    List<LocationTracking> locationTrackingList =
        locationTrackingRepository.findAllByShelterIdAndLastTrackedAtAfter(
            shelterId, lastLocationTrackedAfter
        );

    return locationTrackingList.stream().collect(Collectors.toMap(
        LocationTracking::getHomelessId,
        Function.identity(),
        (lt1, lt2) -> lt2.getLastTrackedAt().isAfter(lt1.getLastTrackedAt()) ? lt2 : lt1
    ));
  }

  /**
   * @return map (key : Homeless id, value : 마지막 LocationTracking)
   */
  @Transactional(readOnly = true)
  public Map<Long, LocationTracking> getAllLast(List<Long> homelessIds) {
    List<LocationTracking> locationTrackingList =
        locationTrackingRepository.findAllLast(homelessIds);

    return locationTrackingList.stream().collect(Collectors.toMap(
        LocationTracking::getHomelessId,
        Function.identity(),
        (lt1, lt2) -> lt2.getLastTrackedAt().isAfter(lt1.getLastTrackedAt()) ? lt2 : lt1
    ));
  }
}
