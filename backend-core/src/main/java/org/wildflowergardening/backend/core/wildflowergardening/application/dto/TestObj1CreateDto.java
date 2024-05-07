package org.wildflowergardening.backend.core.wildflowergardening.application.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Getter
public class TestObj1CreateDto {

  private final BigDecimal value;
  private final ZonedDateTime createdAt;    // 시간 json serialization 관찰용
}
