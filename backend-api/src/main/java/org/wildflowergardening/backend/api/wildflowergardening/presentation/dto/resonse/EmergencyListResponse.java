package org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.resonse;

import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.EmergencyLogItem;

import java.util.List;

public class EmergencyListResponse {
    String result;
    List<EmergencyLogItem> logs;
}
