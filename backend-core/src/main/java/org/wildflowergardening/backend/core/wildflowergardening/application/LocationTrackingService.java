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
import org.wildflowergardening.backend.core.kernel.application.exception.ApplicationLogicException;
import org.wildflowergardening.backend.core.kernel.application.exception.ExceptionType;
import org.wildflowergardening.backend.core.kernel.application.exception.WildflowerExceptionType;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTracking;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTrackingRepository;

import static org.wildflowergardening.backend.core.kernel.application.exception.WildflowerExceptionType.HOMELESS_APP_ESSENTIAL_TERMS_NOT_AGREED;
import static org.wildflowergardening.backend.core.kernel.application.exception.WildflowerExceptionType.HOMELESS_APP_NOT_DATA_LOCATION;

@Service
@RequiredArgsConstructor
public class LocationTrackingService {

    private final LocationTrackingRepository locationTrackingRepository;

    @Transactional
    public Long createOrUpdate(
            Long homelessId, Long shelterId, LocationStatus locationStatus
    ) {
        Optional<LocationTracking> lastLocationOptional = locationTrackingRepository
                .findTopByHomelessIdOrderByIdDesc(homelessId);
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
    public LocationTracking getLocationByHomelessId(long homelessId) {
        Optional<LocationTracking> lastLocationTracking = locationTrackingRepository
                .findTopByHomelessIdOrderByIdDesc(homelessId);

        if (lastLocationTracking.isEmpty()) {
            throw new ApplicationLogicException(HOMELESS_APP_NOT_DATA_LOCATION);
        }

        return lastLocationTracking.get();

    }
}
