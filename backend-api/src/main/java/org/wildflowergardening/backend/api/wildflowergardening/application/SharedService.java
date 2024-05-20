package org.wildflowergardening.backend.api.wildflowergardening.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.ShelterIdNameDto;

@Service
@RequiredArgsConstructor
public class SharedService {

  private final ShelterService shelterService;

  public List<ShelterIdNameDto> getAllIdName() {
    return shelterService.getAll().stream()
        .map(shelter -> ShelterIdNameDto.builder()
            .shelterId(shelter.getId())
            .shelterName(shelter.getName())
            .build())
        .collect(Collectors.toList());
  }
}
