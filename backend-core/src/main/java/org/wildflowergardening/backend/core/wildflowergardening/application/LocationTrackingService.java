package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
            Long homelessId, Long shelterId, InOutStatus inOutStatus
    ) {
        Optional<LocationTracking> lastLocationOptional = locationTrackingRepository
                .findTopByHomelessIdOrderByLastUpdatedAtDesc(homelessId);
        LocalDateTime curTime = LocalDateTime.now();
        if (lastLocationOptional.isEmpty()) {
            return locationTrackingRepository.save(LocationTracking.builder()
                            .homelessId(homelessId)
                            .shelterId(shelterId)
                            .inOutStatus(inOutStatus)
                            .trackedAt(curTime)
                            .build())
                    .getId();
        }
        LocationTracking locationTracking = lastLocationOptional.get();

        locationTracking.setInOutStatus(inOutStatus);
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
                    .inOutStatus(InOutStatus.IN_SHELTER)
                    .trackedAt(curTime)
                    .build());
            return LocationTracking.builder().inOutStatus(InOutStatus.IN_SHELTER).build();
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

    /**
     * @return set(외출 후 당일 ( 4시간) 복귀 하지 않은 homelessId
     */
    @Transactional(readOnly = true)
    public List<LocationTracking> getUnreturnedOutingsHomelessIds(LocalDateTime now) {
        LocalDateTime startTime = now.minusHours(24);
        return locationTrackingRepository.findByLastUpdatedAtBeforeAndInOutStatus(startTime, InOutStatus.OUT_SHELTER);
    }

    @Transactional(readOnly = true)
    public List<LocationTracking> getUnreturnedSleepoversHomelessIds(Set<Long> homelessIds, LocalDateTime now) {
        LocalDateTime startTime = now.toLocalDate().atStartOfDay();
        return locationTrackingRepository.findByHomelessIdInAndLastUpdatedAtBeforeAndInOutStatus(homelessIds, startTime, InOutStatus.OVERNIGHT_STAY);
    }

    @Transactional(readOnly = true)
    public List<LocationTracking> getSleepoverHomeless(Set<Long> homelessIds) {
        return locationTrackingRepository.findByHomelessIdInAndInOutStatusNot(homelessIds, InOutStatus.OVERNIGHT_STAY);
    }

    @Transactional(readOnly = true)
    public Set<Long> getHomelessIdsByInOutStatus(Long shelterId, InOutStatus status) {
        return locationTrackingRepository.findHomelessIdByShelterIdAndInOutStatus(shelterId, status);
    }

    @Transactional
    public void deleteInoutStatus(Long homelessId, Long shelterId) {
        Optional<LocationTracking> locationTrackingOptional
                = locationTrackingRepository.findByHomelessIdAndShelterId(homelessId, shelterId);

        if (locationTrackingOptional.isEmpty()) {
            return;
        }

        locationTrackingRepository.delete(locationTrackingOptional.get());
    }

}