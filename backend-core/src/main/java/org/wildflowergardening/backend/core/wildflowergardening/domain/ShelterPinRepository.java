package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterPinRepository extends JpaRepository<ShelterPin, Long> {

  Optional<ShelterPin> findByShelterId(Long shelterId);
}
