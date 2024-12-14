package org.wildflowergardening.backend.core.wildflowergardening.domain;

public enum ParticipateStatus {
    NONE, PARTICIPATE, NOT_PARTICIPATE;

    public static ParticipateStatus from(String value) {
        try {
            return valueOf(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("ParticipateStatus 에 " + value + "가 존재하지 않습니다.");
        }
    }
}
