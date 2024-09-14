package org.wildflowergardening.backend.core.wildflowergardening.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmergencyLogRepository extends JpaRepository<EmergencyLog, Long> {
//    List<EmergencyLog> findByShelterId(Long shelterId);
}
