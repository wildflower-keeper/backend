package org.wildflowergardening.backend.api.wildflowergardening.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wildflowergardening.backend.api.wildflowergardening.application.SharedService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.ShelterIdNameDto;

@RestController
@RequiredArgsConstructor
@Tag(name = "1. 여러 서비스에서 공통으로 사용하는 API")
public class SharedController {

  private final SharedService sharedService;

  @GetMapping("/api/v1/shelters")
  @Operation(summary = "센터 목록 조회")
  public ResponseEntity<List<ShelterIdNameDto>> getAllIdName() {
    return ResponseEntity.ok(sharedService.getAllIdName());
  }
}
