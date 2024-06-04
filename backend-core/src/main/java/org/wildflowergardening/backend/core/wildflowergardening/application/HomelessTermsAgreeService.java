package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.HomelessTermsAgree;
import org.wildflowergardening.backend.core.wildflowergardening.domain.HomelessTermsAgreeRepository;

@Service
@RequiredArgsConstructor
public class HomelessTermsAgreeService {

  private final HomelessTermsAgreeRepository homelessTermsAgreeRepository;

  @Transactional(readOnly = true)
  public void createTermsAgrees(Long homelessId, Collection<Long> homelessTermsIds) {
    List<HomelessTermsAgree> termsAgrees = homelessTermsIds.stream()
        .map(homelessTermsId -> HomelessTermsAgree.builder()
            .homelessId(homelessId)
            .homelessTermsId(homelessTermsId)
            .build())
        .toList();
    homelessTermsAgreeRepository.saveAll(termsAgrees);
  }
}
