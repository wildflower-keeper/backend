package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ShelterRepository;

@RequiredArgsConstructor
@Service
public class ShelterService {

  private final ShelterRepository shelterRepository;

  @Transactional
  public Long save(Shelter shelter) {
    return shelterRepository.save(shelter).getId();
  }

  @Transactional(readOnly = true)
  public List<Shelter> getAll() {
    return shelterRepository.findAll();
  }

  /**
   * @return 인증 성공 시 인증한 센터, 실패 시 null
   */
  @Transactional(readOnly = true)
  public Optional<Shelter> getShelterById(Long id) {
    return shelterRepository.findById(id);
  }
}
