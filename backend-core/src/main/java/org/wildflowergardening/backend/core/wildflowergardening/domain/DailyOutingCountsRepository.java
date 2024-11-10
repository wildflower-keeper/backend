package org.wildflowergardening.backend.core.wildflowergardening.domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyOutingCountsRepository extends JpaRepository<DailyOutingCounts, Long> {
    @Query("SELECT d.count FROM DailyOutingCounts d " +
            "WHERE d.shelterId = :shelterId " +
            "AND d.recordedDate BETWEEN :startDate AND :endDate " +
            "ORDER BY d.recordedDate")
    List<Long> findMonthlyCountsByShelterIdAndDateRange(
            @Param("shelterId") Long shelterId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    Optional<DailyOutingCounts> findByShelterIdAndRecordedDate(Long shelterId, LocalDate now);
}
