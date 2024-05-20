package org.wildflowergardening.backend.api.wildflowergardening.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.wildflowergardening.backend.api.wildflowergardening.application.WildflowerAdminService;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateShelterRequest;

@RestController
@RequiredArgsConstructor
@Tag(name = "2. 들꽃지기 admin API")
public class WildflowerAdminController {

  private final WildflowerAdminService wildflowerAdminService;

  @PostMapping("/api/v1/admin/shelter")
  @Operation(summary = "센터 생성")
  public ResponseEntity<Long> createShelter(
      @RequestHeader(value = "ADMIN_AUTH") String adminAuth,
      @RequestBody @Valid CreateShelterRequest request
  ) {
    Long shelterId = wildflowerAdminService.createShelter(adminAuth, request);

    return ResponseEntity.ok(shelterId);
  }
}
