package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationTrackingRepository extends JpaRepository<LocationTracking, Long> {

    Optional<LocationTracking> findTopByHomelessIdOrderByLastUpdatedAtDesc(Long homelessId);

    List<LocationTracking> findAllByShelterIdAndTrackedAtAfter(
            Long shelterId, LocalDateTime trackedAtAfter);

    List<LocationTracking> findAllByHomelessIdIn(List<Long> homelessId);

    List<LocationTracking> findAllByHomelessIdInAndTrackedAtAfter(
            List<Long> homelessId, LocalDateTime trackedAt);

    List<LocationTracking> findAllByOrderByHomelessIdAsc();

    List<LocationTracking> findAllByShelterIdOrderByHomelessId(Long shelterId);

    List<LocationTracking> findByLastUpdatedAtBeforeAndInOutStatus(LocalDateTime dateStartTime, InOutStatus status);

    List<LocationTracking> findByHomelessIdInAndLastUpdatedAtBeforeAndInOutStatus(Set<Long> homelessIds, LocalDateTime dateStartTime, InOutStatus status);

    List<LocationTracking> findByHomelessIdInAndInOutStatusNot(Set<Long> homelessIds, InOutStatus status);

    @Query("select l.homelessId from LocationTracking l where l.shelterId = :shelterId "
            + " and l.inOutStatus = :status ")
    Set<Long> findHomelessIdByShelterIdAndInOutStatus(Long shelterId, InOutStatus status);
}
