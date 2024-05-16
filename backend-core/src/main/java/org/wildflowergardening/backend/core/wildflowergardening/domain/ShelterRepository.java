package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {

  Optional<Shelter> findByIdAndPassword(Long id, String password);
}
