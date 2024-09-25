package org.wildflowergardening.backend.core.wildflowergardening.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EmergencyLogRepository extends JpaRepository<EmergencyLog, Long> {
    @Query("SELECT e FROM EmergencyLog e WHERE e.shelter_id = :shelterId ORDER BY e.createdAt DESC")
    List<EmergencyLog> findByShelterIdOrderByCreatedAtDesc(@Param("shelterId") Long shelterId);

    @Query("SELECT e FROM EmergencyLog e WHERE e.createdAt BETWEEN :startDate AND :endDate ORDER BY e.createdAt DESC")
    List<EmergencyLog> findAllByCreatedAtWithinLast24Hours(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
