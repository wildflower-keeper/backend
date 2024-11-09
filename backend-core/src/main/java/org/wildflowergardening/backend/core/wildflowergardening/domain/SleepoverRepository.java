package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Query(" select s.homelessId from Sleepover s "
            + " where s.homelessId in :candidateHomelessIds "
            + " and "
            + " s.startDate <= :targetDate and s.endDate > :targetDate "
            + " and s.deletedAt is null ")
    Set<Long> filterSleepoverHomeless(
            @Param("candidateHomelessIds") List<Long> candidateHomelessIds,
            @Param("targetDate") LocalDate targetDate
    );

    @Query("select s.homelessId from Sleepover s "
            + " where s.startDate<= :targetDate and s.endDate > :targetDate "
            + " and s.deletedAt is null")
    Set<Long> findBySleepoverHomeless(
            @Param("targetDate") LocalDate targetDate
    );

    @Query("select  s.homelessId from Sleepover s "
            + "where s.endDate = :yesterday "
            + "and s.deletedAt is null")
    Set<Long> findHomelessIdsWithSleepoverEndingYesterday(@Param("yesterday") LocalDate yesterday);

    @Query(" select s from Sleepover s "
            + " where s.shelterId = :shelterId and s.deletedAt is null "
            + " order by s.id desc")
    Page<Sleepover> findAllByShelterId(@Param("shelterId") Long shelterId, Pageable pageable);

    @Query(" select s from Sleepover s "
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
            + " where s.homelessId = :homelessId "
            + " and "
            + " s.startDate < :startDateBefore "
            + " and "
            + " s.endDate > :endDateAfter "
            + " and s.deletedAt is null ")
    List<Sleepover> findAllByHomelessAndPeriod(
            @Param("homelessId") Long homelessId,
            @Param("startDateBefore") LocalDate startDateBefore,
            @Param("endDateAfter") LocalDate endDateAfter
    );

    @Query(" select s from Sleepover s "
            + " where s.homelessId = :homelessId "
            + " and s.endDate > :targetDate "
            + " and s.deletedAt is null "
            + " order by s.startDate asc ")
    List<Sleepover> findByHomelessAndEndDateAfter(
            @Param("homelessId") Long homelessId, @Param("targetDate") LocalDate endDateAfter
    );

    @Query(" select s from Sleepover s "
            + " where s.shelterId = :shelterId "
            + " and "
            + " s.createdAt between :createdAtStart and :createdAtEnd ")
    List<Sleepover> findAllByCreatedAtIn(
            @Param("shelterId") Long shelterId,
            @Param("createdAtStart") LocalDateTime createdAtStart,
            @Param("createdAtEnd") LocalDateTime createdAtEnd
    );

    Optional<Sleepover> findTopByHomelessIdAndEndDateAfterAndDeletedAtIsNullOrderByStartDateAsc(
            Long homelessId, LocalDate endDate);
}
