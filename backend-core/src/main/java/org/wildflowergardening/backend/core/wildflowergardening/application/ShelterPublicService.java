package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ShelterPublic;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ShelterPublicRepository;

@Service
@RequiredArgsConstructor
public class ShelterPublicService {

  private final ShelterPublicRepository shelterPublicRepository;

  @Transactional(readOnly = true)
  public Optional<ShelterPublic> getOneByDeviceId(String deviceId) {
    return shelterPublicRepository.findByDeviceId(deviceId);
  }

  @Transactional
  public Long createShelterPublic(ShelterPublic shelterPublic) {
    return shelterPublicRepository.save(shelterPublic).getId();
  }

  @Transactional
  public void updateShelterPublicDeviceName(Long shelterPublicId, String deviceName) {
    ShelterPublic shelterPublic = shelterPublicRepository.findById(shelterPublicId)
        .orElseThrow(() -> new IllegalArgumentException(
            "id=" + shelterPublicId + "인 ShelterPublic 이 존재하지 않습니다."
        ));
    shelterPublic.setDeviceName(deviceName);
  }
}
