package org.wildflowergardening.backend.core.wildflowergardening.application;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.EmergencyLog;
import org.wildflowergardening.backend.core.wildflowergardening.domain.EmergencyLogRepository;

import java.util.List;


@RequiredArgsConstructor
@Service
public class EmergencyService {
    private final EmergencyLogRepository emergencyLogRepository;

    @Transactional
    public Long save(EmergencyLog log) {
        return emergencyLogRepository.save(log).getId();
    }

    public List<EmergencyLog> getEmergencyLogByShelterId(long shelterId) {
        return emergencyLogRepository.findByShelterIdOrderByCreatedAtDesc(shelterId);
    }

}
