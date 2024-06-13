package org.wildflowergardening.backend.api.wildflowergardening.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PhoneNumberFormatterTest {

  @Test
  void format() {
    Assertions.assertEquals(
        PhoneNumberFormatter.format("010-1234-1234"), "01012341234"
    );
    Assertions.assertEquals(
        PhoneNumberFormatter.format(" 01 0-1234-1234"), "01012341234"
    );
  }
}