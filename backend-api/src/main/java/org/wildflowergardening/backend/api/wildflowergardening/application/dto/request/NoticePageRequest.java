package org.wildflowergardening.backend.api.wildflowergardening.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NoticeFilterType;

@Getter
@Builder
public class NoticePageRequest {
    private NoticeFilterType filterType;
    private Boolean isGlobal;
    private String filterValue;
    private Long shelterId;
    private int pageNumber;
    private int pageSize;
}
