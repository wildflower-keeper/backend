package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessPageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.Location;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse.PageInfoResponse;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessQueryService;
import org.wildflowergardening.backend.core.wildflowergardening.application.LocationTrackingService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTracking;

@Component
@RequiredArgsConstructor
public class HomelessNoFilterPager implements HomelessFilterPager {

    private final HomelessQueryService homelessQueryService;
    private final LocationTrackingService locationTrackingService;
    private final SleepoverService sleepoverService;

    @Override
    public NumberPageResponse<HomelessResponse> getPage(HomelessPageRequest pageRequest) {
        NumberPageResult<Homeless> result = homelessQueryService.getPage(
                pageRequest.getShelterId(), pageRequest.getPageNumber(), pageRequest.getPageSize()
        );
        List<Long> homelessIds = result.getItems().stream()
                .map(Homeless::getId)
                .toList();
        Set<Long> sleepoverHomelessIds = sleepoverService.filterSleepoverHomelessIds(
                homelessIds, pageRequest.getTargetDateTime().toLocalDate()
        );
    Map<Long, LocationTracking> lastTrackingMap = locationTrackingService.getAllLastTrackedAfter(
        homelessIds, pageRequest.getTargetDateTime().minusHours(1)
    );

        return NumberPageResponse.<HomelessResponse>builder()
                .items(
                        result.getItems().stream()
                                .sorted(Comparator.comparing(Homeless::getCreatedAt))
                                .map(homeless -> HomelessResponse.from(homeless,
                                        lastTrackingMap.getOrDefault(homeless.getId(), null),
                                        sleepoverHomelessIds.contains(homeless.getId())))
                                .collect(Collectors.toList())
                )
                .pagination(PageInfoResponse.of(result.getPagination()))
                .build();
    }
}
