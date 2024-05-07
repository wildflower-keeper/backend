package org.wildflowergardening.backend.api.wildflowergardening.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.TestResponse;
import org.wildflowergardening.backend.core.wildflowergardening.application.TestObj1Service;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.CursorCreatedAtPageRequest;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.CursorPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.AccountShelter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ShelterRepository;
import org.wildflowergardening.backend.core.wildflowergardening.domain.TestObj1;

@Service
@RequiredArgsConstructor
public class TestQueryService {

  private final TestObj1Service testObj1Service;
  private final ShelterRepository shelterRepository;

  public boolean existTest() {
    List<AccountShelter> all = shelterRepository.findAll();
    return !all.isEmpty();
  }

  public CursorPageResult<TestResponse> getPageInCreatedOrder(CursorCreatedAtPageRequest pageRequest) {
    CursorPageResult<TestObj1> testObj1s = testObj1Service.getPageInCreatedOrder(pageRequest);

    List<TestResponse> results = testObj1s.items().stream()
        .map(testObj1 -> TestResponse.builder()
            .id(testObj1.getId())
            .testData(testObj1.getValue().toPlainString())
            .createdAt(testObj1.getCreatedAt())
            .build())
        .collect(Collectors.toList());

    return new CursorPageResult<>(results,
        testObj1s.lastCreatedAt(), testObj1s.lastId(), testObj1s.pageSize());
  }
}
