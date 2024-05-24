package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless.LocationStatus;

public interface HomelessRepository extends JpaRepository<Homeless, Long> {

  Optional<Homeless> findByDeviceId(String deviceId);

  Page<Homeless> findAllByShelterId(Long shelterId, Pageable pageable);

  Page<Homeless> findAllByShelterIdAndLastLocationStatus(
      Long shelterId, LocationStatus locationStatus, Pageable pageable
  );

  @Query(" select h from Homeless h where h.shelter.id = :shelterId and h.name like %:name%")
  Page<Homeless> findAllByShelterIdAndNameLike(
      @Param("shelterId") Long shelterId, @Param("name") String name, Pageable pageable
  );
}
