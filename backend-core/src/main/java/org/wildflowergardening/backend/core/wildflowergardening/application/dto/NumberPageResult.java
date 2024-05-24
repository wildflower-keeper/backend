package org.wildflowergardening.backend.core.wildflowergardening.application.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@Getter
public class NumberPageResult<T> {

  private List<T> items;
  private PageInfoResult pagination;

  @Builder(access = AccessLevel.PRIVATE)
  @Getter
  public static class PageInfoResult {

    private int currentPageNumber;
    private Integer nextPageNumber;
    private int pageSize;
    private long lastPageNumber;

    public static PageInfoResult of(Page<?> page) {
      int currentPageNumber = page.getNumber() + 1;
      int totalPages = page.getTotalPages();

      return PageInfoResult.builder()
          .currentPageNumber(currentPageNumber)
          .nextPageNumber(currentPageNumber < totalPages ? currentPageNumber + 1 : null)
          .pageSize(page.getSize())
          .lastPageNumber(totalPages)
          .build();
    }
  }
}
