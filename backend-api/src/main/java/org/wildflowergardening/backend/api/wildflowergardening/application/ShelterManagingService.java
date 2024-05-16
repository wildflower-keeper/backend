package org.wildflowergardening.backend.api.wildflowergardening.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateShelterDto;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.SessionDto;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterLoginDto;
import org.wildflowergardening.backend.core.kernel.config.YamlPropertySourceFactory;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.ShelterIdNameDto;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.ShelterIdPasswordDto;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;

@Service
@RequiredArgsConstructor
@PropertySource(
    value = "classpath:application-core-${spring.profiles.active:dev}.yaml",
    factory = YamlPropertySourceFactory.class
)
public class ShelterManagingService {

  private final ShelterService shelterService;

  @Value("${admin.password}")
  private String adminPassword;

  public Long createShelter(String adminPassword, CreateShelterDto dto) {
    if (!this.adminPassword.equals(adminPassword)) {
      throw new IllegalArgumentException("센터 생성 권한이 없습니다.");
    }
    Shelter shelter = Shelter.builder()
        .name(dto.getName())
        .password(dto.getPassword())
        .latitude(dto.getLatitude())
        .longitude(dto.getLongitude())
        .build();
    return shelterService.save(shelter);
  }

  public List<ShelterIdNameDto> getAllIdName() {
    return shelterService.getAll().stream()
        .map(shelter -> ShelterIdNameDto.builder()
            .shelterId(shelter.getId())
            .shelterName(shelter.getName())
            .build())
        .collect(Collectors.toList());
  }

  public SessionDto login(ShelterLoginDto dto) {
    Session session = shelterService.authenticate(ShelterIdPasswordDto.builder()
        .shelterId(dto.getId())
        .password(dto.getPw())    // Todo encrypt
        .build());

    return SessionDto.builder()
        .sessionId(String.valueOf(session.getId()))
        .expiredAt(session.getExpiredAt())
        .build();
  }
}
