package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DutyOfficerRepository extends JpaRepository<DutyOfficer, Long> {

  List<DutyOfficer> findByShelterIdAndTargetDateBetween(
      Long shelterId, LocalDate targetDate, LocalDate targetDate2
  );
}
