package org.wildflowergardening.backend.core.wildflowergardening.application;

import static org.wildflowergardening.backend.core.kernel.application.exception.WildflowerExceptionType.SHELTER_ADMIN_CHIEF_OFFICERS_TOO_MANY;
import static org.wildflowergardening.backend.core.kernel.application.exception.WildflowerExceptionType.SHELTER_ADMIN_CHIEF_PHONE_NUMBER_ALREADY_EXISTS;

import io.micrometer.common.util.StringUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.kernel.application.exception.ApplicationLogicException;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ChiefOfficer;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ChiefOfficerRepository;

@Service
@RequiredArgsConstructor
public class ChiefOfficerService {

  private final ChiefOfficerRepository chiefOfficerRepository;

  @Transactional
  public Long create(Long shelterId, String name, String phoneNumber) {
    List<ChiefOfficer> chiefOfficers = chiefOfficerRepository.findByShelterId(shelterId);

    if (chiefOfficers.size() >= 10) {
      throw new ApplicationLogicException(SHELTER_ADMIN_CHIEF_OFFICERS_TOO_MANY);
    }
    if (chiefOfficers.stream()
        .anyMatch(chiefOfficer -> chiefOfficer.getPhoneNumber().equals(phoneNumber))) {
      throw new ApplicationLogicException(SHELTER_ADMIN_CHIEF_PHONE_NUMBER_ALREADY_EXISTS);
    }
    return chiefOfficerRepository.save(
        ChiefOfficer.builder()
            .shelterId(shelterId)
            .name(name)
            .phoneNumber(phoneNumber)
            .build()
    ).getId();
  }

  @Transactional(readOnly = true)
  public List<ChiefOfficer> getAll(Long shelterId) {
    return chiefOfficerRepository.findByShelterId(shelterId);
  }

  @Transactional
  public void update(Long shelterId, Long chiefOfficerId, String name, String phoneNumber) {
    chiefOfficerRepository.findByIdAndShelterId(chiefOfficerId, shelterId)
        .ifPresent(chiefOfficer -> {
          if (!StringUtils.isEmpty(name)) {
            chiefOfficer.setName(name);
          }
          if (!StringUtils.isEmpty(phoneNumber)) {
            chiefOfficer.setPhoneNumber(phoneNumber);
          }
        });
  }
}
