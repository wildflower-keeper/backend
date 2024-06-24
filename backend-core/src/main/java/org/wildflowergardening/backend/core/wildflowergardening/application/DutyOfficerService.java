package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DutyOfficer;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DutyOfficerRepository;

@Service
@RequiredArgsConstructor
public class DutyOfficerService {

  private final DutyOfficerRepository dutyOfficerRepository;

  @Transactional
  public void create(List<DutyOfficer> dutyOfficers) {
    dutyOfficerRepository.saveAll(dutyOfficers);
  }

  @Transactional(readOnly = true)
  public List<DutyOfficer> getList(Long shelterId, LocalDate startDate, LocalDate endDate) {
    return dutyOfficerRepository.findByShelterIdAndTargetDate(shelterId, startDate, endDate);
  }
}
