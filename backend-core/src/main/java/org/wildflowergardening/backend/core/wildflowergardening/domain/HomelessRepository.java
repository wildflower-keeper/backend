package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomelessRepository extends JpaRepository<Homeless, Long> {

  Optional<Homeless> findByDeviceId(String deviceId);
}
