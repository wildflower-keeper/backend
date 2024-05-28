package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SleepoverRepository extends JpaRepository<Sleepover, Long> {

  Optional<Sleepover> findByIdAndHomelessId(Long id, Long homelessId);

  @Query(" select s.homeless.id from Sleepover s "
      + " where s.homeless.id in :candidateHomelessIds "
      + " and "
      + " s.startDate <= :targetDate and s.endDate > :targetDate "
      + " and s.deletedAt is null ")
  Set<Long> filterSleepoverHomeless(
      @Param("candidateHomelessIds") List<Long> candidateHomelessIds,
      @Param("targetDate") LocalDate targetDate
  );

  @Query(" select s from Sleepover s join fetch s.homeless "
      + " where s.shelterId = :shelterId "
      + " and "
      + " s.startDate <= :targetDate and s.endDate > :targetDate "
      + " and s.deletedAt is null ")
  Page<Sleepover> findAllByShelterIdAndTargetDate(
      @Param("shelterId") Long shelterId, @Param("targetDate") LocalDate targetDate,
      Pageable pageable
  );

  @Query(" select count(s) from Sleepover s "
      + " where s.shelterId = :shelterId"
      + " and "
      + " s.startDate <= :targetDate and s.endDate > :targetDate "
      + " and s.deletedAt is null ")
  Long countByTargetDate(
      @Param("shelterId") Long shelterId, @Param("targetDate") LocalDate targetDate
  );

  @Query(" select s from Sleepover s "
      + " where s.homeless.id = :homelessId "
      + " and "
      + " s.startDate < :startDateBefore "
      + " and "
      + " s.endDate > :endDateAfter "
      + " and s.deletedAt is null ")
  List<Sleepover> findAllByHomelessAndPeriod(
      @Param("homelessId") Long homelessId,
      @Param("startDateBefore") LocalDate startDateBefore, @Param("endDateAfter") LocalDate endDateAfter
  );

  Optional<Sleepover> findTopByHomelessIdAndDeletedAtIsNullAndEndDateAfterOrderByEndDateAsc(
      Long homelessId, LocalDate endDate);
}
