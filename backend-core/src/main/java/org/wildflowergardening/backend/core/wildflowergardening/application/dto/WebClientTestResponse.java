package org.wildflowergardening.backend.core.wildflowergardening.application.dto;

import java.time.ZonedDateTime;

public record WebClientTestResponse(Long id, String testData, ZonedDateTime createdAt) {

}
