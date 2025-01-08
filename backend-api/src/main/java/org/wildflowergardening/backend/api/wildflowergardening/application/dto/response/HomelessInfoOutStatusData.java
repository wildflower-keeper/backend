package org.wildflowergardening.backend.api.wildflowergardening.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class HomelessInfoOutStatusData {
    private Long homelessId;
    private String name;
    private LocalDateTime time;
}
