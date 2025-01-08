package org.wildflowergardening.backend.api.wildflowergardening.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class HomelessInfoPlannedOvernightStateData {
    private Long homelessId;
    private String name;
    private LocalDate plannedDate;
    private int plannedDday;
}
