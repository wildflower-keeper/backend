package org.wildflowergardening.backend.api.kernel.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

  @GetMapping("/api/v1/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("healthy");
  }
}
