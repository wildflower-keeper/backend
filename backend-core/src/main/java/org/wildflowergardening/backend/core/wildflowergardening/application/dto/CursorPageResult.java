package org.wildflowergardening.backend.core.wildflowergardening.application.dto;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @param lastCreatedAt items 가 비어있으면 null
 * @param lastId        items 가 비어있으면 null
 */
public record CursorPageResult<T>(
    List<T> items, ZonedDateTime lastCreatedAt, Long lastId, int pageSize
) {}
