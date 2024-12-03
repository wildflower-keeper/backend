package org.wildflowergardening.backend.core.wildflowergardening.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EmergencyLogRepository extends JpaRepository<EmergencyLog, Long> {
    @Query("SELECT e FROM EmergencyLog e WHERE e.shelterId = :shelterId ORDER BY e.createdAt DESC")
    List<EmergencyLog> findByShelterIdOrderByCreatedAtDesc(@Param("shelterId") Long shelterId);

    List<EmergencyLog> findAllByCreatedAtBetweenAndShelterId(LocalDateTime startDate, LocalDateTime endDate, Long shelterId);


    @Query("SELECT count(e) FROM EmergencyLog e " +
            "WHERE e.shelterId = :shelterId " +
            "AND e.createdAt >= :startOfDay " +
            "AND e.createdAt < :endOfDay")
    Long findCountByShelterIdAndDate(
            @Param("shelterId") Long shelterId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);
}
