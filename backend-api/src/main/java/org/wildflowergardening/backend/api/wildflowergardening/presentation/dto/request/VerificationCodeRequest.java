package org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class VerificationCodeRequest {
    @NotNull(message = "email이 null입니다.")
    @Schema(description = "이메일", example = "email@email.com")
    private String email;

    @NotEmpty(message = "인증 코드가 비었습니다.")
    @Size(max = 11, message = "인증 코드를 10자 이내로 입력해주세요.")
    @Schema(description = "인증 코드", example = "code_example")
    private String code;
}
