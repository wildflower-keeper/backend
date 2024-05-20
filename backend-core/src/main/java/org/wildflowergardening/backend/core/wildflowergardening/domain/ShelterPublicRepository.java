package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShelterPublicRepository extends JpaRepository<ShelterPublic, Long> {

  @Query("select s from ShelterPublic s join fetch s.shelter where s.deviceId = :deviceId")
  Optional<ShelterPublic> findByDeviceId(@Param("deviceId") String deviceId);
}
