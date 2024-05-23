package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NumberPageResponse<T> {

  private List<T> items;

  @Schema(description = "페이지네이션 정보")
  private PageInfoResponse pagination;

  @Builder
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
  }
}
