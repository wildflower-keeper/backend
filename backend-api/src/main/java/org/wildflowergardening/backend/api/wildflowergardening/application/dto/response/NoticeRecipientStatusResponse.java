package org.wildflowergardening.backend.api.wildflowergardening.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class NoticeRecipientStatusResponse {
    @Schema(description = "읽음 현황")
    private List<NoticeRecipientInfoResult> items;

    @Schema(description = "일음 현황 정보")
    private NoticeReadInfo noticeReadInfo;

    @Builder
    @Getter
    public static class NoticeReadInfo {

        @Schema(description = "전체 전달 받은 사람의 수")
        private Long totalCount;

        @Schema(description = "읽은 사람의 수")
        private Long readCount;

        @Schema(description = "안 읽은 사람의 수")
        private Long unReadCount;
    }

}
