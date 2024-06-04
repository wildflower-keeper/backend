package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.HomelessTerms;
import org.wildflowergardening.backend.core.wildflowergardening.domain.HomelessTermsRepository;
import org.wildflowergardening.backend.core.wildflowergardening.domain.dto.HomelessTermsIdEssentialDto;


@Service
@RequiredArgsConstructor
public class HomelessTermsService {

  private final HomelessTermsRepository homelessTermsRepository;

  @Transactional
  public Long create(HomelessTerms homelessTerms) {
    return homelessTermsRepository.save(homelessTerms)
        .getId();
  }

  @Transactional(readOnly = true)
  public List<HomelessTerms> findAll(LocalDate targetDate) {
    return homelessTermsRepository.findAll(targetDate);
  }

  /**
   * @return map key : HomelessTerms id, value : 약관 동의 필수 여부
   */
  @Transactional(readOnly = true)
  public Map<Long, Boolean> findAllIdEssential(LocalDate targetDate) {
    return homelessTermsRepository.findAllIdEssential(targetDate).stream()
        .collect(Collectors.toMap(
            HomelessTermsIdEssentialDto::getId,
            HomelessTermsIdEssentialDto::getIsEssential
        ));
  }
}
