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
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.CreateOrUpdateLocationTrackingDto;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTracking;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTrackingRepository;

@Service
@RequiredArgsConstructor
public class LocationTrackingService {

  private final LocationTrackingRepository locationTrackingRepository;

  @Transactional
  public void createOrUpdate(CreateOrUpdateLocationTrackingDto dto) {
    Optional<LocationTracking> lastLocationOptional = locationTrackingRepository
        .findTopByHomelessIdOrderByIdDesc(dto.getHomelessId());

    // create
    if (lastLocationOptional.isEmpty()
        || lastLocationOptional.get().getLocationStatus() != dto.getLocationStatus()) {
      locationTrackingRepository.save(LocationTracking.builder()
          .homelessId(dto.getHomelessId())
          .shelterId(dto.getShelterId())
          .locationStatus(dto.getLocationStatus())
          .lastLatitude(dto.getLatitude())
          .lastLongitude(dto.getLongitude())
          .build());
      return;
    }
    // update
    LocationTracking lastLocation = lastLocationOptional.get();

    lastLocation.setLastLatitude(dto.getLatitude());
    lastLocation.setLastLongitude(dto.getLongitude());
    lastLocation.setLastTrackedAt(dto.getLastTrackedAt());
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
