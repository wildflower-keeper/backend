package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.CreateSleepoverDto;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult.PageInfoResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.HomelessRepository;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Sleepover;
import org.wildflowergardening.backend.core.wildflowergardening.domain.SleepoverRepository;

@Service
@RequiredArgsConstructor
public class SleepoverService {

  private final HomelessRepository homelessRepository;
  private final SleepoverRepository sleepOverRepository;

  @Transactional
  public Long create(CreateSleepoverDto dto) {
    // 외박 기간 중복 검사
    List<Sleepover> overlapped = sleepOverRepository.findAllByHomelessAndPeriod(
        dto.getHomelessId(), dto.getEndDate(), dto.getStartDate()
    );
    if (!overlapped.isEmpty()) {
      throw new IllegalArgumentException("기간이 중복되는 외박 신청 내역이 있습니다.");
    }
    // 외박 신청 create
    Homeless homeless = homelessRepository.findById(dto.getHomelessId())
        .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

    Sleepover sleepover = Sleepover.builder()
        .creatorType(dto.getCreatorType())
        .shelterId(dto.getShelterId())
        .homeless(homeless)
        .startDate(dto.getStartDate())
        .endDate(dto.getEndDate())
        .build();
    return sleepOverRepository.save(sleepover).getId();
  }

  @Transactional(readOnly = true)
  public List<Sleepover> getSleepoversForPeriod(
      Long homelessId, LocalDate periodStart, LocalDate periodEnd
  ) {
    return sleepOverRepository.findAllByHomelessAndPeriod(
        homelessId, periodEnd, periodStart);
  }

  /**
   * @param candidateHomelessIds targetDate 에 외박신청했는지 알고싶은 노숙인 ids
   * @param sleepoverTargetDate  외박 신청 확인 기준일
   * @return candidateHomelessIds 중에서 오늘 외박 신청한 노숙인 ids
   */
  @Transactional(readOnly = true)
  public Set<Long> filterSleepoverHomelessIds(
      List<Long> candidateHomelessIds, LocalDate sleepoverTargetDate
  ) {
    return sleepOverRepository.filterSleepoverHomeless(
        candidateHomelessIds, sleepoverTargetDate
    );
  }

  @Transactional(readOnly = true)
  public NumberPageResult<Sleepover> getPage(
      Long shelterId, LocalDate sleepoverTargetDate, int pageNumber, int pageSize
  ) {
    PageRequest pageRequest = PageRequest.of(
        pageNumber - 1, pageSize, Sort.by(Direction.DESC, "id")
    );
    Page<Sleepover> sleepoverPage = sleepOverRepository.findAllByShelterIdAndTargetDate(
        shelterId, sleepoverTargetDate, pageRequest
    );
    return NumberPageResult.<Sleepover>builder()
        .items(sleepoverPage.getContent())
        .pagination(PageInfoResult.of(sleepoverPage))
        .build();
  }

  @Transactional(readOnly = true)
  public long count(Long shelterId, LocalDate sleepoverTargetDate) {
    Long count = sleepOverRepository.countByTargetDate(shelterId, sleepoverTargetDate);
    return count != null ? count : 0;
  }
}
