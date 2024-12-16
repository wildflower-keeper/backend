package org.wildflowergardening.backend.core.wildflowergardening.domain;

public enum NotificationMessageType {
    REMIND_SLEEPOVER, REMIND_RETURN;

    public static NotificationMessageType from(String value) {
        try {
            return valueOf(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("NotificationMessageType 에 " + value + "가 존재하지 않습니다.");
        }
    }
}
