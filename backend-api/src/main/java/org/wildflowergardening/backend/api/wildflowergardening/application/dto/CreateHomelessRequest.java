package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class CreateHomelessRequest {

  @NotBlank
  @Size(max = 255)
  @Schema(description = "노숙인 성함", example = "홍길동")
  private String name;

  @NotNull
  @Schema(description = "센터 id", example = "1")
  private Long shelterId;

  @NotNull
  @Schema(description = "센터 password", example = "password_example")
  private String shelterPw;

  @Schema(description = "노숙인의 스마트폰 identifier", example = "test_device_id")
  private String deviceId;

  @Schema(description = "노숙인 방번호 (선택사항)", example = "방번호 (선택사항)")
  private String room;

  @Schema(description = "노숙인 생년월일 (선택사항)", example = "1970-05-15")
  private LocalDate birthDate;

  @Schema(description = "노숙인 휴대폰번호 (선택사항)", example = "010-0000-0000")
  private String phoneNumber;

  @Schema(description = "센터 입소일 (선택사항)", example = "2024-08-01")
  private LocalDate admissionDate;
}
