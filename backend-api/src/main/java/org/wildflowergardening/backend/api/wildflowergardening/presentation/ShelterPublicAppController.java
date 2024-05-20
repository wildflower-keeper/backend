package org.wildflowergardening.backend.api.wildflowergardening.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wildflowergardening.backend.api.wildflowergardening.application.ShelterPublicAppService;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.ShelterPublicAuthInterceptor;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.ShelterPublicAuthorized;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateShelterPublicRequest;
import org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.ShelterPublicResponse;
import org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.UpdateShelterPublicDeviceNameRequest;
import org.wildflowergardening.backend.core.wildflowergardening.application.UserContextHolder;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.ShelterPublicUserContext;

@RestController
@RequiredArgsConstructor
@Tag(name = "센터 공용 노숙인 서비스 API")
public class ShelterPublicAppController {

  private final ShelterPublicAppService shelterPublicAppService;
  private final UserContextHolder userContextHolder;

  @Operation(summary = "센터 공용 노숙인 서비스 기기 연결")
  @PostMapping("/api/v1/shelter-public")
  public ResponseEntity<Long> createShelterPublic(
      @RequestBody @Valid CreateShelterPublicRequest request
  ) {
    Long shelterPublicId = shelterPublicAppService.createShelterPublic(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(shelterPublicId);
  }

  @Operation(summary = "센터 공용 노숙인 서비스 접속 정보 조회")
  @GetMapping("/api/v1/shelter-public")
  @Parameters(@Parameter(
      name = ShelterPublicAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "test_shelter_device_id"
  ))
  @ShelterPublicAuthorized
  public ResponseEntity<ShelterPublicResponse> getOneByDeviceId() {
    ShelterPublicUserContext shelterPublic = (ShelterPublicUserContext) userContextHolder.getUserContext();
    return ResponseEntity.ok(ShelterPublicResponse.builder()
        .shelterPublicId(shelterPublic.getShelterPublicId())
        .shelterId(shelterPublic.getShelterId())
        .shelterName(shelterPublic.getShelterName())
        .deviceName(shelterPublic.getDeviceName())
        .build());
  }

  @Operation(summary = "센터 공용 노숙인 서비스 기기 별명 수정")
  @PatchMapping("/api/v1/shelter-public/device-name")
  @Parameters(@Parameter(
      name = ShelterPublicAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "test_shelter_device_id"
  ))
  @ShelterPublicAuthorized
  public ResponseEntity<Void> updateShelterPublicDeviceName(
      @RequestBody @Valid UpdateShelterPublicDeviceNameRequest request
  ) {
    ShelterPublicUserContext shelterPublic = (ShelterPublicUserContext) userContextHolder.getUserContext();
    shelterPublicAppService.updateShelterPublicDeviceName(
        shelterPublic.getShelterPublicId(), request.getDeviceName()
    );
    return ResponseEntity.ok().build();
  }
}
