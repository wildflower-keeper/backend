package org.wildflowergardening.backend.api.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateShelterPublicRequest;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterPublicService;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ShelterPublic;

@RequiredArgsConstructor
@Service
public class ShelterPublicAppService {

  private final ShelterService shelterService;
  private final ShelterPublicService shelterPublicService;
  private final SleepoverService sleepoverService;

  @Transactional
  public Long createShelterPublic(CreateShelterPublicRequest request) {
    Shelter shelter = shelterService.getShelterById(request.getShelterId())
        .orElseThrow(() -> new IllegalArgumentException("센터 id가 존재하지 않습니다."));

    boolean isAlreadyPresent = shelterPublicService.getOneByDeviceId(request.getDeviceId())
        .isPresent();

    if (isAlreadyPresent) {
      throw new IllegalArgumentException("해당 디바이스는 이미 센터 공용 서비스 이용자로 등록되어 있습니다.");
    }
    return shelterPublicService.createShelterPublic(
        ShelterPublic.builder()
            .shelter(shelter)
            .deviceId(request.getDeviceId())
            .deviceName(null)
            .build()
    );
  }

  public void updateShelterPublicDeviceName(Long shelterPublicId, String newDeviceName) {
    shelterPublicService.updateShelterPublicDeviceName(shelterPublicId, newDeviceName);
  }
}
