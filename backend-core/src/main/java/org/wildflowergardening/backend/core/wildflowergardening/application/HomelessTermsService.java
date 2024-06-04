package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.HomelessTerms;
import org.wildflowergardening.backend.core.wildflowergardening.domain.HomelessTermsRepository;

@Service
@RequiredArgsConstructor
public class HomelessTermsService {

  private final HomelessTermsRepository homelessTermsRepository;

  @Transactional
  public Long create(HomelessTerms homelessTerms) {
    return homelessTermsRepository.save(homelessTerms)
        .getId();
  }
}
