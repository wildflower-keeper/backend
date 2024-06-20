package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ChiefOfficer;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ChiefOfficerRepository;

@Service
@RequiredArgsConstructor
public class ChiefOfficerService {

  private final ChiefOfficerRepository chiefOfficerRepository;

  @Transactional
  public Long createOrUpdate(Long shelterId, String name, String phoneNumber) {
    Optional<ChiefOfficer> chiefOfficerOptional = chiefOfficerRepository.findByShelterId(shelterId);

    if (chiefOfficerOptional.isEmpty()) {
      return chiefOfficerRepository.save(
          ChiefOfficer.builder()
              .shelterId(shelterId)
              .name(name)
              .phoneNumber(phoneNumber)
              .build()
      ).getId();
    }
    ChiefOfficer chiefOfficer = chiefOfficerOptional.get();
    chiefOfficer.setName(name);
    chiefOfficer.setPhoneNumber(phoneNumber);
    return chiefOfficer.getId();
  }
}
