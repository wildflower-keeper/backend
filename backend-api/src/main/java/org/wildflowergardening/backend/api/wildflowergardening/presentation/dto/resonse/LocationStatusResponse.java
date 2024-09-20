package org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.resonse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationStatusResponse {
    String locationStatus;
}
