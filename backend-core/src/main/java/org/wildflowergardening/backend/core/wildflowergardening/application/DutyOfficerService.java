package org.wildflowergardening.backend.core.wildflowergardening.application;

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
}
