package org.wildflowergardening.backend.core.wildflowergardening.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ChiefOfficer;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyHomelessCountsRepository extends JpaRepository<DailyHomelessCounts, Long> {
    @Query("SELECT d.count FROM DailyHomelessCounts d " +
            "WHERE d.shelterId = :shelterId " +
            "AND d.recordedDate BETWEEN :startDate AND :endDate " +
            "ORDER BY d.recordedDate")
    List<Long> findMonthlyCountsByShelterIdAndDateRange(
            @Param("shelterId") Long shelterId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<DailyHomelessCounts> findByShelterIdAndRecordedDateIsBetween(Long shelterId, LocalDate startDate, LocalDate endDate);
    Optional<DailyHomelessCounts> findByShelterIdAndRecordedDate(Long shelterId, LocalDate now);
}
