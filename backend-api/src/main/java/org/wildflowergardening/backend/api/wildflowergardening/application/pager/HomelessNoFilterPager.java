package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessPageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse.PageInfoResponse;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessQueryService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;

@Component
@RequiredArgsConstructor
public class HomelessNoFilterPager implements HomelessFilterPager {

  private final HomelessQueryService homelessQueryService;
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
        homelessIds, pageRequest.getTargetDate()
    );
    return NumberPageResponse.<HomelessResponse>builder()
        .items(
            result.getItems().stream()
                .map(homeless -> HomelessResponse.builder()
                    .id(homeless.getId())
                    .name(homeless.getName())
                    .room(homeless.getRoom())
                    .targetDateSleepover(sleepoverHomelessIds.contains(homeless.getId()))
                    .birthDate(homeless.getBirthDate())
                    .phoneNumber(homeless.getPhoneNumber())
                    .admissionDate(homeless.getAdmissionDate())
                    .lastLocationStatus(homeless.getLastLocationStatus())
                    .lastLocationTrackedAt(homeless.getLastLocationTrackedAt())
                    .build())
                .collect(Collectors.toList())
        )
        .pagination(PageInfoResponse.of(result.getPagination()))
        .build();
  }
}
