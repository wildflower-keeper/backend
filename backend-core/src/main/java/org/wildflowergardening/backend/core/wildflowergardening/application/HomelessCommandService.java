package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless.LocationStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.HomelessRepository;

@Service
@RequiredArgsConstructor
public class HomelessCommandService {

  private final HomelessRepository homelessRepository;

  @Transactional
  public Long create(Homeless homeless) {
    Optional<Homeless> homelessOptional = homelessRepository.findByDeviceId(
        homeless.getDeviceId()
    );
    if (homelessOptional.isPresent()) {
      throw new IllegalArgumentException("해당 디바이스로 이미 등록된 계정이 있습니다.");
    }
    return homelessRepository.save(homeless).getId();
  }

  @Transactional
  public void updateLocationStatus(
      Long homelessId, LocationStatus lastLocationStatus, LocalDateTime lastLocationTrackedAt
  ) {
    Homeless homeless = homelessRepository.findById(homelessId)
        .orElseThrow(() -> new IllegalArgumentException("Homeless not found"));

    homeless.setLastLocationStatus(lastLocationStatus);
    homeless.setLastLocationTrackedAt(lastLocationTrackedAt);
  }
}
