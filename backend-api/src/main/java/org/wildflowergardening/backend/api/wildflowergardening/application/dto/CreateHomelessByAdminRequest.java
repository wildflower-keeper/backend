package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class CreateHomelessByAdminRequest {


  @NotBlank(message = "성함이 비어있습니다.")
  @Size(max = 255, message = "성함은 255자 이내로 입력해야 합니다.")
  @Schema(description = "노숙인 성함", example = "홍길동")
  private String name;

  @NotNull(message = "센터 id가 null 입니다.")
  @Schema(description = "센터 id", example = "1")
  private Long shelterId;

  @NotNull(message = "센터 pin 이 null 입니다.")
  @Schema(description = "센터 pin", example = "1234")
  private String shelterPin;

  @NotBlank(message = "방 번호가 비었습니다.")
  @Size(max = 255, message = "방번호는 255자 이내로 입력해주세요.")
  @Schema(description = "노숙인 방번호", example = "방번호 (선택사항)", nullable = true)
  private String room;

  @Schema(description = "노숙인 생년월일 (선택사항)", example = "1970-05-15", nullable = true)
  private LocalDate birthDate;

  @Schema(description = "노숙인 휴대폰번호 (선택사항)", example = "01012341234", nullable = true)
  private String phoneNumber;

  @Schema(description = "센터 입소일 (선택사항)", example = "2024-08-01", nullable = true)
  private LocalDate admissionDate;
}
