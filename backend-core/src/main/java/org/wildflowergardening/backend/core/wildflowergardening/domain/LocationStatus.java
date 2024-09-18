package org.wildflowergardening.backend.core.wildflowergardening.domain;

public enum LocationStatus {
  IN_SHELTER, OUT_SHELTER;

  public static LocationStatus from(String value) {
    try {
      return valueOf(value);
    } catch (Exception e) {
      throw new IllegalArgumentException("LocationStatus 에 " + value + "가 존재하지 않습니다.");
    }
  }
}
