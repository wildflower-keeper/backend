package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessPageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse.PageInfoResponse;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Sleepover;

@Component
@RequiredArgsConstructor
public class HomelessSleepoverFilterPager implements HomelessFilterPager {

  private final SleepoverService sleepoverService;

  @Override
  public NumberPageResponse<HomelessResponse> getPage(HomelessPageRequest pageRequest) {
    NumberPageResult<Sleepover> result = sleepoverService.getPage(
        pageRequest.getShelterId(), pageRequest.getTargetDate(),
        pageRequest.getPageNumber(), pageRequest.getPageSize()
    );
    return NumberPageResponse.<HomelessResponse>builder()
        .items(
            result.getItems().stream()
                .map(sleepover -> {
                  Homeless homeless = sleepover.getHomeless();

                  return HomelessResponse.builder()
                      .id(homeless.getId())
                      .name(homeless.getName())
                      .room(homeless.getRoom())
                      .targetDateSleepover(true)
                      .birthDate(homeless.getBirthDate())
                      .phoneNumber(homeless.getPhoneNumber())
                      .admissionDate(homeless.getAdmissionDate())
                      .lastLocationStatus(homeless.getLastLocationStatus())
                      .lastLocationTrackedAt(homeless.getLastLocationTrackedAt())
                      .build();
                })
                .toList()
        )
        .pagination(PageInfoResponse.of(result.getPagination()))
        .build();

  }
}
