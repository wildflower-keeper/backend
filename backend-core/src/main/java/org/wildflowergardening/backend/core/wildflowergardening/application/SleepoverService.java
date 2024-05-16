package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Sleepover;
import org.wildflowergardening.backend.core.wildflowergardening.domain.SleepoverRepository;

@Service
@RequiredArgsConstructor
public class SleepoverService {

  private final SleepoverRepository sleepOverRepository;

  @Transactional
  public Long create(Sleepover sleepover) {
    return sleepOverRepository.save(sleepover).getId();
  }
}
