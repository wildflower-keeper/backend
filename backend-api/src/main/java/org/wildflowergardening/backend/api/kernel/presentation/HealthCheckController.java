package org.wildflowergardening.backend.api.kernel.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

  private final ServerProperties serverProperties;

  @GetMapping("/api/v1/system/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("healthy");
  }

  @GetMapping("/api/v1/system/port")
  public ResponseEntity<Integer> currentPort() {
    return ResponseEntity.ok(serverProperties.getPort());
  }
}
