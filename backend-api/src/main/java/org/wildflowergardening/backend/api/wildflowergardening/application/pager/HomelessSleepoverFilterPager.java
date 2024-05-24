package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessPageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse.PageInfoResponse;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Sleepover;

@Component
@RequiredArgsConstructor
public class HomelessSleepoverFilterPager implements HomelessFilterPager {

  private final SleepoverService sleepoverService;

  @Override
  public NumberPageResponse<HomelessResponse> getPage(HomelessPageRequest pageRequest) {
    NumberPageResult<Sleepover> result = sleepoverService.getPage(
        pageRequest.getShelterId(), pageRequest.getSleepoverTargetDate(),
        pageRequest.getPageNumber(), pageRequest.getPageSize()
    );
    return NumberPageResponse.<HomelessResponse>builder()
        .items(
            result.getItems().stream()
                .map(sleepover -> HomelessResponse.from(sleepover.getHomeless(), true))
                .toList()
        )
        .pagination(PageInfoResponse.of(result.getPagination()))
        .build();
  }
}
