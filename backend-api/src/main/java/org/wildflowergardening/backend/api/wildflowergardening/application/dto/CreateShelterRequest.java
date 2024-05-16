package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class CreateShelterRequest {

  @NotBlank
  @Size(max = 255)
  @Schema(description = "센터명", example = "서울특별시립 비전트레이닝센터")
  private String name;

  @NotBlank
  @Size(max = 25, min = 4)
  @Schema(description = "센터명", example = "password_example")
  private String password;

  @NotNull
  @DecimalMax("90")
  @DecimalMin("-90")
  @Schema(description = "위도")
  private BigDecimal latitude;

  @NotNull
  @DecimalMax("180")
  @DecimalMin("-180")
  @Schema(description = "경도")
  private BigDecimal longitude;
}
