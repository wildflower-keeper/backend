package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import lombok.*;

@Getter
@ToString
@Builder
public class FcmSendDto {
    private String token;
    private String title;
    private String body;
    private Data data;

    @Getter
    @ToString
    @Builder
    public static class Data {
        private String screen;
        private Long noticeId;
    }
}