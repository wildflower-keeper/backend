package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTracking;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTrackingRepository;

@Service
@RequiredArgsConstructor
public class LocationTrackingService {

  private final LocationTrackingRepository locationTrackingRepository;

  @Transactional
  public Long createOrUpdate(
      Long homelessId, Long shelterId, LocationStatus locationStatus, LocalDateTime trackedAt
  ) {
    Optional<LocationTracking> lastLocationOptional = locationTrackingRepository
        .findTopByHomelessIdOrderByIdDesc(homelessId);

    if (lastLocationOptional.isEmpty()) {
      return locationTrackingRepository.save(LocationTracking.builder()
              .homelessId(homelessId)
              .shelterId(shelterId)
              .locationStatus(locationStatus)
              .trackedAt(trackedAt)
              .build())
          .getId();
    }
    LocationTracking locationTracking = lastLocationOptional.get();

    locationTracking.setLocationStatus(locationStatus);
    locationTracking.setTrackedAt(trackedAt);

    return locationTracking.getId();
  }

  /**
   * @return map (key : Homeless id, value : 마지막 LocationTracking)
   */
  @Transactional(readOnly = true)
  public Map<Long, LocationTracking> getAllLastByLastTrackedAfter(
      Long shelterId, LocalDateTime lastLocationTrackedAfter
  ) {
    List<LocationTracking> locationTrackingList =
        locationTrackingRepository.findAllByShelterIdAndTrackedAtAfter(
            shelterId, lastLocationTrackedAfter
        );

    return locationTrackingList.stream().collect(Collectors.toMap(
        LocationTracking::getHomelessId,
        Function.identity()
    ));
  }

  /**
   * @return map (key : Homeless id, value : 마지막 LocationTracking)
   */
  @Transactional(readOnly = true)
  public Map<Long, LocationTracking> getAllLastTrackedAfter(
      List<Long> homelessIds, LocalDateTime trackedAtAfter
  ) {
    List<LocationTracking> locationTrackingList = locationTrackingRepository
        .findAllByHomelessIdInAndTrackedAtAfter(homelessIds, trackedAtAfter);

    return locationTrackingList.stream().collect(Collectors.toMap(
        LocationTracking::getHomelessId,
        Function.identity()
    ));
  }
}
