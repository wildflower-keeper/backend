package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드는 제외
public class FcmMessageRequest {
    private Message message;
    private boolean validateOnly;

    @Getter
    @Builder
    public static class Message {
        private String token;
        private Notification notification;
        private Map<String, String> data;

        @Getter
        @Builder
        public static class Notification {
            private String title;
            private String body;
        }
    }
}