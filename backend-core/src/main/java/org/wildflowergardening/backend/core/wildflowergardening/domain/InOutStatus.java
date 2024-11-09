package org.wildflowergardening.backend.core.wildflowergardening.domain;

public enum InOutStatus {
    IN_SHELTER, OUT_SHELTER, OVERNIGHT_STAY, UNCONFIRMED;

    public static InOutStatus from(String value) {
        try {
            return valueOf(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("InOutStatus 에 " + value + "가 존재하지 않습니다.");
        }
    }
}
