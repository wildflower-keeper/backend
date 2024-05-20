package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ShelterPublic;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ShelterPublicRepository;

@Service
@RequiredArgsConstructor
public class ShelterPublicService {

  private final ShelterPublicRepository shelterPublicRepository;

  @Transactional
  public Long createShelterPublic(ShelterPublic shelterPublic) {
    return shelterPublicRepository.save(shelterPublic).getId();
  }
}
