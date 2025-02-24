package org.wildflowergardening.backend.api.wildflowergardening.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;

import java.util.List;

@Builder
@Getter
public class HomelessInfoPageResponse<T> {

    private String type;

    private SummeryResponse summery;

    private List<T> data;

    @Schema(description = "페이지네이션 정보")
    private PageInfoResponse pagination;

    @Builder
    @Getter
    public static class SummeryResponse {
        @Schema(description = "전체 아이템 수")
        private Long totalCount;
    }

    @Builder(access = AccessLevel.PRIVATE)
    @Getter
    public static class PageInfoResponse {

        @Schema(description = "현재 페이지 번호")
        private int currentPageNumber;

        @Schema(description = "다음 페이지 번호 (다음 페이지가 없는 경우 null)")
        private Integer nextPageNumber;

        @Schema(description = "페이지 당 item 최대 갯수")
        private int pageSize;

        @Schema(description = "마지막 페이지 번호")
        private long lastPageNumber;

        public static PageInfoResponse of(NumberPageResult.PageInfoResult pageInfoResult) {
            return PageInfoResponse.builder()
                    .currentPageNumber(pageInfoResult.getCurrentPageNumber())
                    .nextPageNumber(pageInfoResult.getNextPageNumber())
                    .pageSize(pageInfoResult.getPageSize())
                    .lastPageNumber(pageInfoResult.getLastPageNumber())
                    .build();
        }
    }
}
