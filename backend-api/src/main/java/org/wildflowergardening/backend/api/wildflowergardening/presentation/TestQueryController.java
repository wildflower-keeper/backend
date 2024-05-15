package org.wildflowergardening.backend.api.wildflowergardening.presentation;

import io.swagger.v3.oas.annotations.Parameter;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wildflowergardening.backend.api.wildflowergardening.application.TestQueryService;
import org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.TestResponse;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.CursorCreatedAtPageRequest;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.CursorPageResult;

@Slf4j
@RestController
@RequestMapping("/api/v1/skeleton")
@RequiredArgsConstructor
public class TestQueryController {

  private final TestQueryService testQueryService;

  @GetMapping("/test-db")
  public ResponseEntity<Boolean> testDB() {
    return ResponseEntity.ok(testQueryService.existTest());
  }

  @GetMapping("/tests")
  public ResponseEntity<CursorPageResult<TestResponse>> test(
      @RequestHeader @Parameter(example = "kiel0103@naver.com") String tester,
      @RequestParam(required = false) ZonedDateTime lastCreatedAt,
      @RequestParam(required = false) Long lastId,
      @RequestParam(defaultValue = "20") int pageSize
  ) {
    log.debug("## tester : {}", tester);
    CursorCreatedAtPageRequest request = CursorCreatedAtPageRequest.builder()
        .lastCreatedAt(lastCreatedAt)
        .lastId(lastId)
        .pageSize(pageSize)
        .build();
    return ResponseEntity.ok()
        .body(testQueryService.getPageInCreatedOrder(request));
  }
}
