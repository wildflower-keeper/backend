package org.wildflowergardening.backend.core.wildflowergardening.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationStatus;

@Getter
@Builder
public class CreateOrUpdateLocationTrackingDto {

  private Long homelessId;
  private Long shelterId;
  private LocationStatus locationStatus;
  private BigDecimal latitude;
  private BigDecimal longitude;
  private LocalDateTime lastTrackedAt;
}
