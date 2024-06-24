package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DutyOfficerRepository extends JpaRepository<DutyOfficer, Long> {

  @Query(" select d from DutyOfficer d where d.shelterId=:shelterId "
      + " and :startDate <= d.targetDate "
      + " and d.targetDate <= :endDate")
  List<DutyOfficer> findByShelterIdAndTargetDate(
      Long shelterId, LocalDate startDate, LocalDate endDate
  );
}
