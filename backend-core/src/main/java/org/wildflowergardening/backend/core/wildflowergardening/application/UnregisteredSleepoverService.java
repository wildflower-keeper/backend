package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.UnregisteredSleepover;
import org.wildflowergardening.backend.core.wildflowergardening.domain.UnregisteredSleepoverRepository;

@Service
@RequiredArgsConstructor
public class UnregisteredSleepoverService {

  private final UnregisteredSleepoverRepository unregisteredSleepoverRepository;

  @Transactional
  public void createOrUpdate(UnregisteredSleepover unregisteredSleepover) {
    unregisteredSleepoverRepository.save(unregisteredSleepover);
  }
}
