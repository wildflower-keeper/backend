package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.InOutStatus;

@Getter
@Builder
public class HomelessPageRequest {

    private HomelessFilterType filterType;
    private InOutStatus inOutstatus;
    private String filterValue;
    private Long shelterId;
    private int pageNumber;
    private int pageSize;
    private LocalDateTime targetDateTime;
}
