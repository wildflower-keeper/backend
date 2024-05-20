package org.wildflowergardening.backend.core.wildflowergardening.application.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NumberPageResult<T> {

  private List<T> items;
  private NumberPageNext next;

  @Builder
  @Getter
  public static class NumberPageNext {

    private Integer nextPageNumber;
    private int nextPageSize;
    private long lastPageNumber;
  }
}
