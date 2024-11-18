package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class CreateShelterRequest {

    @NotBlank(message = "센터명이 비어있습니다.")
    @Size(max = 255, message = "센터명은 255자 이하로 제한됩니다.")
    @Schema(description = "센터명", example = "서울특별시립 비전트레이닝센터")
    private String name;

    @Schema(description = "센터 전화번호(중복 불가)", example = "01012341231", nullable = true)
    private String phoneNumber;

    @NotNull(message = "위도가 null 일 수 없습니다.")
    @DecimalMax(value = "90", message = "위도 최댓값은 90 입니다.")
    @DecimalMin(value = "-90", message = "위도 최솟값은 -90 입니다.")
    @Digits(integer = 2, fraction = 6, message = "위도는 정수 두자리 이하, 소수점 6자리 이하로 지정 가능합니다.")
    @Schema(description = "위도")
    private BigDecimal latitude;

    @NotNull(message = "경도가 null 일 수 없습니다.")
    @DecimalMax(value = "180", message = "경도 최댓값은 180 입니다.")
    @DecimalMin(value = "-180", message = "경도 최솟값은 -180 입니다.")
    @Digits(integer = 3, fraction = 6, message = "경도는 정수 세자리 이하, 소수점 6자리 이하로 지정 가능합니다.")
    @Schema(description = "경도")
    private BigDecimal longitude;
}
