package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.HomelessInfoPageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.HomelessInfoOutStatusData;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.HomelessInfoPageResponse;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessQueryService;
import org.wildflowergardening.backend.core.wildflowergardening.application.LocationTrackingService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.InOutStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTracking;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HomelessInfoOutStateFilterPager implements HomelessInfoFilterPager<HomelessInfoOutStatusData> {
    private final HomelessQueryService homelessQueryService;
    private final LocationTrackingService locationTrackingService;

    @Override
    public HomelessInfoPageResponse<HomelessInfoOutStatusData> getPage(HomelessInfoPageRequest pageRequest) {

        //외출 중인 노숙인 가져오기
        Set<Long> candidateHomelessIds = locationTrackingService.getHomelessIdsByInOutStatus(pageRequest.getShelterId(), InOutStatus.OUT_SHELTER);
        NumberPageResult<Homeless> result = homelessQueryService.getPage(candidateHomelessIds, pageRequest.getPageNumber(), pageRequest.getPageSize());
        List<Long> homelessIds = result.getItems().stream()
                .map(Homeless::getId).toList();
        Map<Long, LocationTracking> lastTrackingMap = locationTrackingService.getAll(
                homelessIds
        );

        return HomelessInfoPageResponse.<HomelessInfoOutStatusData>builder()
                .data(
                        result.getItems().stream()
                                .sorted(Comparator.comparing(Homeless::getCreatedAt))
                                .map(homeless -> HomelessInfoOutStatusData.builder()
                                        .homelessId(homeless.getId())
                                        .name(homeless.getName())
                                        .time(lastTrackingMap.get(homeless.getId()).getTrackedAt())
                                        .build())
                                .collect(Collectors.toList())
                )
                .pagination(HomelessInfoPageResponse.PageInfoResponse.of(result.getPagination()))
                .type("out")
                .summery(HomelessInfoPageResponse.SummeryResponse.builder().totalCount((long) candidateHomelessIds.size()).build())
                .build();
    }

}
