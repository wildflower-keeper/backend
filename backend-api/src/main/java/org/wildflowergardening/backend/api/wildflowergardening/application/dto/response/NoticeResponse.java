package org.wildflowergardening.backend.api.wildflowergardening.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeResponse {

    private Long id;

    @Schema(description = "제목", example = "공지사항 제목")
    private String title;

    @Schema(description = "내용", example = "공지사항 내용")
    private String contents;

    @Schema(description = "발송 시간", example = "2024-11-01 13:33:06.292907")
    private LocalDateTime sendAt;
}