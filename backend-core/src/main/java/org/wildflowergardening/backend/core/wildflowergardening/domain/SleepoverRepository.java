package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SleepoverRepository extends JpaRepository<Sleepover, Long> {

  /*
   외박 신청 시작일이 targetDate 이하 AND 외박 신청 종료일이 targetDate 초과
   */
  @Query(" select s.homeless.id from Sleepover s "
      + " where s.homeless.id in :candidateHomelessIds "
      + " and "
      + " s.startDate <= :targetDate and s.endDate > :targetDate ")
  Set<Long> filterSleepoverHomeless(
      @Param("candidateHomelessIds") List<Long> candidateHomelessIds,
      @Param("targetDate") LocalDate targetDate
  );

  @Query(" select s from Sleepover s join fetch s.homeless "
      + " where s.shelterId = :shelterId "
      + " and "
      + " s.startDate <= :targetDate and s.endDate > :targetDate ")
  Page<Sleepover> findAllByShelterIdAndTargetDate(
      @Param("shelterId") Long shelterId, @Param("targetDate") LocalDate targetDate,
      Pageable pageable
  );

  @Query(" select count(s) from Sleepover s "
      + " where s.shelterId = :shelterId"
      + " and "
      + " s.startDate <= :targetDate and s.endDate > :targetDate ")
  Long countByTargetDate(
      @Param("shelterId") Long shelterId, @Param("targetDate") LocalDate targetDate
  );
}
