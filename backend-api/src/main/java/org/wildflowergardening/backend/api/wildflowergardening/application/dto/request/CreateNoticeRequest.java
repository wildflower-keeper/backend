package org.wildflowergardening.backend.api.wildflowergardening.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateNoticeRequest {

    @NotBlank(message = "공지사항의 제목이 공백일 수는 없습니다.")
    @Schema(description = "제목")
    private String title;

    @NotBlank(message = "공지사항의 내용이 공백일 수는 없습니다.")
    @Schema(description = "내용")
    private String content;

    @NotNull(message = "타겟 유저가 null일 수는 없습니다.")
    @Schema(description = "공지사항 수신 자")
    private List<Long> targetHomelessIds;
}
