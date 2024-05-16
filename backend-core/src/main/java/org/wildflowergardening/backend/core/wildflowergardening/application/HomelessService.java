package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.HomelessRepository;

@Service
@RequiredArgsConstructor
public class HomelessService {

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

  @Transactional(readOnly = true)
  public Optional<Homeless> getOneByDeviceId(String deviceId) {
    return homelessRepository.findByDeviceId(deviceId);
  }
}
