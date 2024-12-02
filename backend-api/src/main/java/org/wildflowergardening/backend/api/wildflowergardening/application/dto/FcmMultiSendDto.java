package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
public class FcmMultiSendDto {
    private List<String> tokens;
    private String title;
    private String body;
    private FcmSendDto.Data data;
}