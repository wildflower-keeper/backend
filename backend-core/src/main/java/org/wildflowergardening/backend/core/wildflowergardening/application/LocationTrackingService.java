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
import org.wildflowergardening.backend.core.wildflowergardening.domain.*;

@Service
@RequiredArgsConstructor
public class LocationTrackingService {

    private final LocationTrackingRepository locationTrackingRepository;

    @Transactional
    public Long createOrUpdate(
            Long homelessId, Long shelterId, LocationStatus locationStatus
    ) {
        Optional<LocationTracking> lastLocationOptional = locationTrackingRepository
                .findTopByHomelessIdOrderByLastUpdatedAtDesc(homelessId);
        LocalDateTime curTime = LocalDateTime.now();
        if (lastLocationOptional.isEmpty()) {
            return locationTrackingRepository.save(LocationTracking.builder()
                            .homelessId(homelessId)
                            .shelterId(shelterId)
                            .locationStatus(locationStatus)
                            .trackedAt(curTime)
                            .build())
                    .getId();
        }
        LocationTracking locationTracking = lastLocationOptional.get();

        locationTracking.setLocationStatus(locationStatus);
        locationTracking.setTrackedAt(curTime);

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

    @Transactional(readOnly = true)
    public Map<Long, LocationTracking> getAll(
            List<Long> homelessIds
    ) {
        List<LocationTracking> locationTrackingList = locationTrackingRepository
                .findAllByHomelessIdIn(homelessIds);

        return locationTrackingList.stream().collect(Collectors.toMap(
                LocationTracking::getHomelessId,
                Function.identity()
        ));
    }

    @Transactional(readOnly = true)
    public LocationTracking getLocationByHomelessId(long homelessId, long shelterId) {
        Optional<LocationTracking> lastLocationTracking = locationTrackingRepository
                .findTopByHomelessIdOrderByLastUpdatedAtDesc(homelessId);

        if (lastLocationTracking.isEmpty()) {
            LocalDateTime curTime = LocalDateTime.now();
            locationTrackingRepository.save(LocationTracking.builder()
                    .homelessId(homelessId)
                    .shelterId(shelterId)
                    .locationStatus(LocationStatus.IN_SHELTER)
                    .trackedAt(curTime)
                    .build());
            return LocationTracking.builder().locationStatus(LocationStatus.IN_SHELTER).build();
        }

        return lastLocationTracking.get();
    }

    @Transactional(readOnly = true)
    public Map<Long, LocationTracking> findAllByOrderByHomelessIdAsc() {
        List<LocationTracking> locationTrackingList = locationTrackingRepository.findAllByOrderByHomelessIdAsc();

        return locationTrackingList.stream().collect(Collectors.toMap(
                LocationTracking::getHomelessId,
                Function.identity()
        ));
    }

    @Transactional(readOnly = true)
    public Map<Long, LocationTracking> findAllByShelterId(Long shelterId) {
        List<LocationTracking> locationTrackingList = locationTrackingRepository.findAllByShelterIdOrderByHomelessId(shelterId);
        return locationTrackingList.stream().collect(Collectors.toMap(
                LocationTracking::getHomelessId,
                Function.identity()
        ));
    }


}