package org.wildflowergardening.backend.core.wildflowergardening.application.dto;

import java.time.ZonedDateTime;
import lombok.Builder;

/**
 * @param lastCreatedAt 첫페이지면 null
 * @param lastId        첫페이지면 null
 */
@Builder
public record CursorCreatedAtPageRequest(
    ZonedDateTime lastCreatedAt, Long lastId, int pageSize
) {}
