package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.HomelessInfoPageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.HomelessInfoPageResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.HomelessInfoOvernightStateData;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessQueryService;
import org.wildflowergardening.backend.core.wildflowergardening.application.LocationTrackingService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.InOutStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Sleepover;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HomelessInfoOvernightFilterPager implements HomelessInfoFilterPager<HomelessInfoOvernightStateData> {
    private final HomelessQueryService homelessQueryService;
    private final LocationTrackingService locationTrackingService;
    private final SleepoverService sleepoverService;

    @Override
    public HomelessInfoPageResponse<HomelessInfoOvernightStateData> getPage(HomelessInfoPageRequest pageRequest) {

        //외박 중인 사람 골라오기
        Set<Long> candidateHomelessIds = locationTrackingService.getHomelessIdsByInOutStatus(pageRequest.getShelterId(), InOutStatus.OVERNIGHT_STAY);
        NumberPageResult<Homeless> result = homelessQueryService.getPage(candidateHomelessIds, pageRequest.getPageNumber(), pageRequest.getPageSize());
        List<Long> homelessIds = result.getItems().stream()
                .map(Homeless::getId).toList();
        Map<Long, Sleepover> sleepoverMap = sleepoverService.getAll(homelessIds);

        return HomelessInfoPageResponse.<HomelessInfoOvernightStateData>builder()
                .data(
                        result.getItems().stream()
                                .sorted(Comparator.comparing(Homeless::getCreatedAt))
                                .map(homeless -> HomelessInfoOvernightStateData.builder()
                                        .homelessId(homeless.getId())
                                        .name(homeless.getName())
                                        .returnDate(sleepoverMap.get(homeless.getId()).getEndDate())
                                        .returnDday((int) ChronoUnit.DAYS.between(LocalDate.now(), sleepoverMap.get(homeless.getId()).getEndDate()))
                                        .build())
                                .collect(Collectors.toList())
                )
                .pagination(HomelessInfoPageResponse.PageInfoResponse.of(result.getPagination()))
                .type("sleepover")
                .summery(HomelessInfoPageResponse.SummeryResponse.builder().totalCount((long) candidateHomelessIds.size()).build())
                .build();
    }
}
