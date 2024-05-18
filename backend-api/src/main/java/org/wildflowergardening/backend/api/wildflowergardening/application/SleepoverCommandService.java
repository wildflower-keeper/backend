package org.wildflowergardening.backend.api.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateSleepoverRequest;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Outing;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.HomelessUserContext;

@RequiredArgsConstructor
@Service
public class SleepoverCommandService {

  private final SleepoverService sleepoverService;

  /*
   노숙인 스마트폰 앱에서 외박신청
   */
  public Long createSleepover(
      HomelessUserContext homeless, CreateSleepoverRequest request
  ) {
    if (!homeless.getHomelessId().equals(request.getHomelessId())) {
      throw new IllegalArgumentException();
    }
    return sleepoverService.create(Outing.builder()
        .outingType(Outing.Type.SLEEPOVER)
        .homelessId(homeless.getHomelessId())
        .homelessName(homeless.getHomelessName())
        .phoneNumber(homeless.getPhoneNumber())
        .shelterId(homeless.getShelterId())
        .startDate(request.getStartDate())
        .endDate(request.getEndDate())
        .build());
  }

  /*
   센터 노숙인 공용 계정으로 외박신청
   */
  public Long createSleepover() {
    return null;
  }
}
