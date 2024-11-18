package org.wildflowergardening.backend.api.wildflowergardening.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ShelterAccountRequest {

    @NotBlank(message = "이메일이 비었습니다.")
    @Schema(description = "이메일", example = "example@example.com")
    private String email;

    @NotBlank(message = "비밀번호가 비었습니다.")
    @Schema(description = "비밀번호", example = "password_example")
    private String password;

    @Schema(description = "전화번호(선택사항)", example = "01012341122")
    private String phoneNumber;

    @NotBlank(message = "이름이 비었습니다.")
    @Schema(description = "이름", example = "김보통")
    private String name;

    @Schema(description = "기타 메모 사항", example = "")
    private String remark;
}
