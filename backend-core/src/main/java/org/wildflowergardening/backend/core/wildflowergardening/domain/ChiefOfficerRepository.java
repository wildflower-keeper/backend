package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChiefOfficerRepository extends JpaRepository<ChiefOfficer, Long> {

  List<ChiefOfficer> findByShelterId(Long shelterId);

  Optional<ChiefOfficer> findByIdAndShelterId(Long id, Long shelterId);
}
