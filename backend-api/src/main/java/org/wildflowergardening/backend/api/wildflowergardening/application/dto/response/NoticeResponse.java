package org.wildflowergardening.backend.api.wildflowergardening.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeResponse {

    private Long noticeId;

    @Schema(description = "제목", example = "공지사항 제목")
    private String title;

    @Schema(description = "내용", example = "공지사항 내용")
    private String contents;

    @Schema(description = "공지 사항 발송 시간", example = "2024-11-01 13:33:06.292907")
    private LocalDateTime createdAt;

    @Schema(description = "이미지 url", example = "url")
    private String imageUrl;

    @Schema(description = "설문 유무", example = "false")
    private Boolean isSurvey;

    @Schema(description = "전체 공지 유무", example = "false")
    private Boolean isGlobal;

    @Schema(description = "읽은 사람의 수", example = "15")
    private Long readCount;

    @Schema(description = "전체 대상자 수", example = "30")
    private Long totalCount;
}
