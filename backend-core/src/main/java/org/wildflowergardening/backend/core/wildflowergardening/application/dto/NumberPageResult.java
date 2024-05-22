package org.wildflowergardening.backend.core.wildflowergardening.application.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NumberPageResult<T> {

  private List<T> items;
  private PageInfoResult pagination;

  @Builder
  @Getter
  public static class PageInfoResult {

    private int currentPageNumber;
    private Integer nextPageNumber;
    private int pageSize;
    private long lastPageNumber;
  }
}
