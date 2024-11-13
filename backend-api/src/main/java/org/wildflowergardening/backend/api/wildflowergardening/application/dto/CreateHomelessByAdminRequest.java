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

/*    @NotNull(message = "센터 pin 이 null 입니다.")
    @Schema(description = "센터 pin", example = "1234")
    private String shelterPin;*/

    @Size(max = 255, message = "방번호는 255자 이내로 입력해주세요.")
    @Schema(description = "노숙인 방번호(필수)", example = "방번호", nullable = true)
    private String room;

    @Schema(description = "노숙인 생년월일 (선택사항)", example = "1970-05-15", nullable = true)
    private LocalDate birthDate;

    @Schema(description = "노숙인 휴대폰번호 (선택사항)", example = "01012341234", nullable = true)
    private String phoneNumber;

/*    @Schema(description = "센터 입소일 (선택사항)", example = "2024-08-01(공란이면 현재 날짜로 등록)", nullable = true)
    private LocalDate admissionDate;*/

    @Schema(description = "메모(선택 사항)", example = "알러지 존재", nullable = true)
    private String memo;
}
