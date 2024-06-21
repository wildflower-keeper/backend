package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChiefOfficerRepository extends JpaRepository<ChiefOfficer, Long> {

  @Query(" select c from ChiefOfficer c "
      + " where c.shelterId=:shelterId and "
      + " (c.lastDate is null or c.lastDate > :lastDateAfter) ")
  List<ChiefOfficer> findByShelterId(
      @Param("shelterId") Long shelterId, @Param("lastDateAfter") LocalDate lastDateAfter
  );
}
