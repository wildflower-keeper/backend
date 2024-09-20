package org.wildflowergardening.backend.api.wildflowergardening.presentation.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ChiefOfficerResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.DutyOfficerResponse;

@Getter
@Builder
public class ShelterInfoResponse {

    private String shelterName;
    private String shelterPhone;
    private List<ChiefOfficerResponse> chiefOfficers;
    private List<DutyOfficerResponse> dutyOfficers;
}
