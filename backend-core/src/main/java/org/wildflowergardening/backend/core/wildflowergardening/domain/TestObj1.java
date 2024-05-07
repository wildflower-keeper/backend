package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Getter
public class TestObj1 {

  private final Long id;
  private final BigDecimal value;
  private final ZonedDateTime createdAt;
}
