package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless.LocationStatus;

public interface HomelessRepository extends JpaRepository<Homeless, Long> {

  Optional<Homeless> findByDeviceId(String deviceId);

  Page<Homeless> findAllByShelterId(Long shelterId, Pageable pageable);

  Page<Homeless> findAllByShelterIdAndLastLocationStatus(
      Long shelterId, LocationStatus locationStatus, Pageable pageable
  );
}
