package org.wildflowergardening.backend.api.wildflowergardening.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeRecipientInfoResult {
    private Long homelessId;
    private String homelessName;
    private String homelessPhoneNumber;
    private boolean isRead;
}
