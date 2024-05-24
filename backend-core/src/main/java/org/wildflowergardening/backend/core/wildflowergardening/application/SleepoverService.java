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
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult.PageInfoResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Sleepover;
import org.wildflowergardening.backend.core.wildflowergardening.domain.SleepoverRepository;

@Service
@RequiredArgsConstructor
public class SleepoverService {

  private final SleepoverRepository sleepOverRepository;

  @Transactional
  public Long create(Sleepover sleepover) {
    return sleepOverRepository.save(sleepover).getId();
  }

  /**
   * @param candidateHomelessIds targetDate 에 외박신청했는지 알고싶은 노숙인 ids
   * @param targetDate           대상 날짜
   * @return candidateHomelessIds 중에서 오늘 외박 신청한 노숙인 ids
   */
  @Transactional(readOnly = true)
  public Set<Long> filterSleepoverHomelessIds(
      List<Long> candidateHomelessIds, LocalDate targetDate
  ) {
    return sleepOverRepository.filterSleepoverHomeless(
        candidateHomelessIds, targetDate
    );
  }

  @Transactional(readOnly = true)
  public NumberPageResult<Sleepover> getPage(
      Long shelterId, LocalDate targetDate, int pageNumber, int pageSize
  ) {
    PageRequest pageRequest = PageRequest.of(
        pageNumber - 1, pageSize, Sort.by(Direction.DESC, "id")
    );
    Page<Sleepover> sleepoverPage = sleepOverRepository.findAllByShelterIdAndTargetDate(
        shelterId, targetDate, pageRequest
    );
    return NumberPageResult.<Sleepover>builder()
        .items(sleepoverPage.getContent())
        .pagination(PageInfoResult.of(sleepoverPage))
        .build();
  }
}
