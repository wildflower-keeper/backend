package org.wildflowergardening.backend.api.wildflowergardening.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationStatus;

@Getter
public class UpdateLocationRequest {

  @NotNull(message = "locationStatus 가 null 일 수 없습니다.")
  @Schema(description = "IN_SHELTER:재실중, OUTING:외출중", example = "IN_SHELTER")
  private LocationStatus locationStatus;

  @DecimalMax(value = "90", message = "위도 최댓값은 90 입니다.")
  @DecimalMin(value = "-90", message = "위도 최솟값은 -90 입니다.")
  @Digits(integer = 2, fraction = 6, message = "위도는 정수 두자리 이하, 소수점 6자리 이하로 지정 가능합니다.")
  @Schema(description = "위도")
  private BigDecimal latitude;

  @DecimalMax(value = "180", message = "경도 최댓값은 180 입니다.")
  @DecimalMin(value = "-180", message = "경도 최솟값은 -180 입니다.")
  @Digits(integer = 3, fraction = 6, message = "경도는 정수 세자리 이하, 소수점 6자리 이하로 지정 가능합니다.")
  @Schema(description = "경도")
  private BigDecimal longitude;
}
