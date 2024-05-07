package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.CursorPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.WebClientTestResponse;

@Service
@RequiredArgsConstructor
public class WebclientTestService {

  private final WebClient testSkeletonWebClient;

  public CursorPageResult<WebClientTestResponse> callGetTwice() {
    return testSkeletonWebClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/api/v1/skeleton/tests")
            .queryParam("pageSize", 1)
            .build())
        .header("tester", "kiel0103@naver.com")
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<CursorPageResult<WebClientTestResponse>>() {
        })
        .block();
  }
}
