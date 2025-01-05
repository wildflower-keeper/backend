package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.HomelessInfoPageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.HomelessInfoPageResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.HomelessInfoPlannedOvernightStateData;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessQueryService;
import org.wildflowergardening.backend.core.wildflowergardening.application.LocationTrackingService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.InOutStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTracking;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Sleepover;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HomelessInfoPlannedOvernightFilterPager implements HomelessInfoFilterPager<HomelessInfoPlannedOvernightStateData> {
    private final HomelessQueryService homelessQueryService;
    private final SleepoverService sleepoverService;
    private final LocationTrackingService locationTrackingService;

    @Override
    public HomelessInfoPageResponse<HomelessInfoPlannedOvernightStateData> getPage(HomelessInfoPageRequest pageRequest) {
        Set<Long> candidateHomelessIds = locationTrackingService.getHomelessIdsByInOutStatus(
                pageRequest.getShelterId(),
                Arrays.asList(InOutStatus.IN_SHELTER, InOutStatus.OUT_SHELTER)
        );

        Set<Long> overnightHomelessIds = locationTrackingService.getHomelessIdsByInOutStatus(pageRequest.getShelterId(), InOutStatus.OVERNIGHT_STAY);

        NumberPageResult<Sleepover> result = sleepoverService.getUpcomingSleepoverPage(candidateHomelessIds, overnightHomelessIds, LocalDate.now(), pageRequest.getPageNumber(), pageRequest.getPageSize());

        return HomelessInfoPageResponse.<HomelessInfoPlannedOvernightStateData>builder()
                .data(
                        result.getItems().stream()
                                .sorted(Comparator.comparing(Sleepover::getStartDate))
                                .map(sleepover ->
                                        HomelessInfoPlannedOvernightStateData.builder()
                                                .homelessId(sleepover.getHomelessId())
                                                .name(sleepover.getHomelessName())
                                                .plannedDate(sleepover.getStartDate())
                                                .plannedDday((int) ChronoUnit.DAYS.between(LocalDate.now(), sleepover.getStartDate()))
                                                .build()
                                )
                                .collect(Collectors.toList())
                )
                .pagination(HomelessInfoPageResponse.PageInfoResponse.of(result.getPagination()))
                .type("plannedOvernight")
                .summery(HomelessInfoPageResponse.SummeryResponse.builder().totalCount((long) result.getItems().size()).build())
                .build();
    }

}
