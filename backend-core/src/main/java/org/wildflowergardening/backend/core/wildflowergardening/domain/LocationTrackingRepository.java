package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationTrackingRepository extends JpaRepository<LocationTracking, Long> {

    Optional<LocationTracking> findTopByHomelessIdOrderByLastUpdatedAtDesc(Long homelessId);

    List<LocationTracking> findAllByShelterIdAndTrackedAtAfter(
            Long shelterId, LocalDateTime trackedAtAfter);

    List<LocationTracking> findAllByHomelessIdIn(List<Long> homelessId);

    List<LocationTracking> findAllByHomelessIdInAndTrackedAtAfter(
            List<Long> homelessId, LocalDateTime trackedAt);
    List<LocationTracking> findAllByOrderByHomelessIdAsc();
    List<LocationTracking> findAllByShelterIdOrderByHomelessId(Long shelterId);
}
