package org.wildflowergardening.backend.core.wildflowergardening.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.core.wildflowergardening.domain.EmergencyLog;
import org.wildflowergardening.backend.core.wildflowergardening.domain.EmergencyLogRepository;

@RequiredArgsConstructor
@Service
public class EmergencyService {
    private final EmergencyLogRepository emergencyLogRepository;
    @Transactional
    public Long save(EmergencyLog log){
        return emergencyLogRepository.save(log).getId();
    }

}
