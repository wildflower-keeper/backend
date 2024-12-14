package org.wildflowergardening.backend.api.wildflowergardening.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class NoticeItemResponse {
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

    @Schema(description = "태그", example = "[\"전체인원\", \"미확인인원\", \"미참여 인원\"]")
    private List<String> tags;

    @Schema(description = "읽은 사람의 id", example = "[\"1\", \"2\"]")
    private List<Long> readHomelessIds;

    @Schema(description = "안읽은 사람의 id", example = "[\"3\", \"4\"]")
    private List<Long> unreadHomelessIds;

    @Schema(description = "미참여 사람의 id", example = "[\"1\", \"2\"]")
    private List<Long> notParticipateHomelessIds;
}
