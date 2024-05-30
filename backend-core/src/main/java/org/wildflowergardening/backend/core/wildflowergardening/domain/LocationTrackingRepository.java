package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationTrackingRepository extends JpaRepository<LocationTracking, Long> {

  Optional<LocationTracking> findTopByHomelessIdOrderByIdDesc(Long homelessId);

  List<LocationTracking> findAllByShelterIdAndLastTrackedAtAfter(
      Long shelterId, LocalDateTime lastTrackedAt);

  @Query(" select lt from LocationTracking lt where lt.id in "
      + " (select max(l.id) from LocationTracking l "
      + " where l.homelessId in :homelessIds "
      + " group by l.homelessId) ")
  List<LocationTracking> findAllLast(@Param("homelessIds") List<Long> homelessIds);
}
