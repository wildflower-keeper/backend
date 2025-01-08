package org.wildflowergardening.backend.api.wildflowergardening.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessInfoFilterType;

@Getter
@Builder
public class HomelessInfoPageRequest {
    private HomelessInfoFilterType filterType;
    private Long shelterId;
    private int pageNumber;
    private int pageSize;
}
