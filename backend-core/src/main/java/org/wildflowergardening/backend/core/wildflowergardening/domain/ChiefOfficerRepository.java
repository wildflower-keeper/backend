package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChiefOfficerRepository extends JpaRepository<ChiefOfficer, Long> {

  Optional<ChiefOfficer> findByShelterId(Long shelterId);
}
