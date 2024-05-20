package org.wildflowergardening.backend.api.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;

@RequiredArgsConstructor
@Service
public class ShelterPublicAppService {

  private final SleepoverService sleepoverService;
}
