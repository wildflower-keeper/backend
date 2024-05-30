package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessPageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse.PageInfoResponse;
import org.wildflowergardening.backend.core.wildflowergardening.application.LocationTrackingService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTracking;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Sleepover;

@Component
@RequiredArgsConstructor
public class HomelessSleepoverFilterPager implements HomelessFilterPager {

  private final SleepoverService sleepoverService;
  private final LocationTrackingService locationTrackingService;

  @Override
  public NumberPageResponse<HomelessResponse> getPage(HomelessPageRequest pageRequest) {
    NumberPageResult<Sleepover> result = sleepoverService.getPage(
        pageRequest.getShelterId(), pageRequest.getSleepoverTargetDate(),
        pageRequest.getPageNumber(), pageRequest.getPageSize()
    );
    List<Long> homelessIds = result.getItems().stream()
        .map(sleepover -> sleepover.getHomeless().getId())
        .toList();
    Map<Long, LocationTracking> lastTrackingMap = locationTrackingService.getAllLast(homelessIds);

    return NumberPageResponse.<HomelessResponse>builder()
        .items(
            result.getItems().stream()
                .map(sleepover -> HomelessResponse.from(
                    sleepover.getHomeless(),
                    lastTrackingMap.getOrDefault(sleepover.getHomeless().getId(), null),
                    true
                ))
                .toList()
        )
        .pagination(PageInfoResponse.of(result.getPagination()))
        .build();
  }
}
