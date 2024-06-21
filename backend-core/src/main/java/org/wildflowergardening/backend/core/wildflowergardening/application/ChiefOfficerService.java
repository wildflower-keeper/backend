package org.wildflowergardening.backend.core.wildflowergardening.application;

import static org.wildflowergardening.backend.core.kernel.application.exception.WildflowerExceptionType.SHELTER_ADMIN_CHIEF_OFFICERS_TOO_MANY;
import static org.wildflowergardening.backend.core.kernel.application.exception.WildflowerExceptionType.SHELTER_ADMIN_CHIEF_PHONE_NUMBER_ALREADY_EXISTS;

import java.time.LocalDate;
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
  public Long create(Long shelterId, String name, String phoneNumber, LocalDate endDate) {
    LocalDate now = LocalDate.now();

    List<ChiefOfficer> chiefOfficers =
        chiefOfficerRepository.findByShelterId(shelterId, now);

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
            .lastDate(endDate)
            .build()
    ).getId();
  }
}
