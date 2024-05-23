package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HomelessPageResponse {

  @Schema(description = "노숙인 목록")
  private List<HomelessResponse> items;

  @Schema(description = "페이지네이션 정보")
  private PageInfoResponse pagination;

  @Schema(description = "외박 신청 여부 조회 기준이 된 현재 날짜")
  private LocalDate today;

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
