package org.wildflowergardening.backend.api.wildflowergardening.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class HomelessInfoOvernightStateData {
    private Long homelessId;
    private String name;
    private LocalDate returnDate;
    private int returnDday;
}
