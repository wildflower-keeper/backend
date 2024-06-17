package org.wildflowergardening.backend.core.wildflowergardening.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class SleepoverTest {

  @Test
  void cancelableAt() {
    Sleepover sleepover = Sleepover.builder()
        .startDate(LocalDate.of(2024, 6, 18))
        .endDate(LocalDate.of(2024, 6, 19))
        .build();
    assertTrue(sleepover.cancelableAt(LocalDate.of(2024, 6, 17)));
    assertTrue(sleepover.cancelableAt(LocalDate.of(2024, 6, 18)));
    assertFalse(sleepover.cancelableAt(LocalDate.of(2024, 6, 19)));
    assertFalse(sleepover.cancelableAt(LocalDate.of(2024, 6, 20)));
  }
}