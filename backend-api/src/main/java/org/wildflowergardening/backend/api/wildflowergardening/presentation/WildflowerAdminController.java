package org.wildflowergardening.backend.api.wildflowergardening.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.wildflowergardening.backend.api.wildflowergardening.application.WildflowerAdminService;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateHomelessTermsRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateShelterRequest;

@RestController
@RequiredArgsConstructor
@Tag(name = "들꽃지기 admin API")
public class WildflowerAdminController {

  private final WildflowerAdminService wildflowerAdminService;

  @PostMapping("/api/v1/wildflower-admin/shelter")
  @Operation(summary = "센터 생성")
  public ResponseEntity<Long> createShelter(
      @RequestHeader(value = "auth-token") @Parameter(example = "test") String adminAuth,
      @RequestBody @Valid CreateShelterRequest request
  ) {
    Long shelterId = wildflowerAdminService.createShelter(adminAuth, request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(shelterId);
  }

  @PostMapping("/api/v1/wildflower-admin/homeless-terms")
  @Operation(summary = "노숙인 앱 약관 생성")
  public ResponseEntity<Long> createHomelessTerms(
      @RequestHeader(value = "auth-token") @Parameter(example = "test") String adminAuth,
      @RequestBody CreateHomelessTermsRequest request
  ) {
    Long homelessTermsId = wildflowerAdminService.createHomelessTerms(adminAuth, request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(homelessTermsId);
  }

  @PostMapping("/api/v1/wildflower-admin/shelter/{shelterId}/password")
  public ResponseEntity<Void> changeShelterPassword(
      @RequestHeader(value = "auth-token") @Parameter(example = "test") String adminAuth,
      @PathVariable Long shelterId,
      @RequestBody String newShelterPw
  ) {
    wildflowerAdminService.changeShelterPassword(adminAuth, shelterId, newShelterPw);
    return ResponseEntity.ok().build();
  }
}
