package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FcmSendDto {
    private String token;
    private String title;
    private String body;
    private Data data;

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Data {
        private String screen;
        private Long noticeId;
    }
}