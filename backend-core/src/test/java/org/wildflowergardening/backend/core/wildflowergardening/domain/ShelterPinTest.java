package org.wildflowergardening.backend.core.wildflowergardening.domain;

import org.junit.jupiter.api.Test;

class ShelterPinTest {

  @Test
  void generatePin() {
    String s = ShelterPin.generatePin();
    while(!s.startsWith("0") && !s.startsWith(" ")) {
      s = ShelterPin.generatePin();
    }
    System.out.println(s);
  }
}