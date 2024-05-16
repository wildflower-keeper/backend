package org.wildflowergardening.backend.api.wildflowergardening.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateShelterRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.SessionResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterLoginRequest;
import org.wildflowergardening.backend.core.kernel.config.YamlPropertySourceFactory;
import org.wildflowergardening.backend.core.wildflowergardening.application.SessionService;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.ShelterIdNameDto;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.ShelterIdPasswordDto;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.UserRole;

@Service
@RequiredArgsConstructor
@PropertySource(
    value = "classpath:application-core-${spring.profiles.active:dev}.yaml",
    factory = YamlPropertySourceFactory.class
)
public class ShelterManagingService {

  private final ShelterService shelterService;
  private final SessionService sessionService;

  @Value("${admin.password}")
  private String adminPassword;

  public Long createShelter(String adminPassword, CreateShelterRequest dto) {
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

  public SessionResponse login(ShelterLoginRequest dto) {
    Optional<Shelter> shelterOptional = shelterService.getShelterByAuthInfo(
        ShelterIdPasswordDto.builder()
            .shelterId(dto.getId())
            .password(dto.getPw())    // Todo encrypt
            .build());

    if (shelterOptional.isEmpty()) {
      throw new IllegalArgumentException("authentication failed");
    }

    Shelter shelter = shelterOptional.get();
    LocalDateTime now = LocalDateTime.now();

    Session session = sessionService.save(Session.builder()
        .userRole(UserRole.SHELTER)
        .userId(shelter.getId())
        .username(shelter.getName())
        .createdAt(now)
        .expiredAt(now.plusHours(1))
        .build());

    return SessionResponse.builder()
        .sessionId(String.valueOf(session.getId()))
        .expiredAt(session.getExpiredAt())
        .build();
  }
}
