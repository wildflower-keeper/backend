package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnregisteredSleepoverRepository extends JpaRepository<UnregisteredSleepover, Long> {

  Optional<UnregisteredSleepover> findByHomelessIdAndTargetDate(
      Long homeless_id, LocalDate targetDate);
}
