package org.wildflowergardening.backend.api.wildflowergardening.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.Location;

import java.time.LocalDateTime;

@Getter
@Builder
public class EmergencyLogItem {
    private Long id;
    private String name;
    private String phNumber;
    private LocalDateTime date;
    private Location location;

}
