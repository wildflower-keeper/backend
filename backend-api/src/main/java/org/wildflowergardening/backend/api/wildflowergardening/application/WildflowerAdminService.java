package org.wildflowergardening.backend.api.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.api.kernel.application.exception.ForbiddenException;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateShelterRequest;
import org.wildflowergardening.backend.core.kernel.config.YamlPropertySourceFactory;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterService;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;

@Service
@RequiredArgsConstructor
@PropertySource(
    value = "classpath:application-core-${spring.profiles.active:dev}.yaml",
    factory = YamlPropertySourceFactory.class
)
public class WildflowerAdminService {


  private final ShelterService shelterService;
  private final PasswordEncoder passwordEncoder;

  @Value("${admin.password}")
  private String adminPassword;

  public Long createShelter(String adminPassword, CreateShelterRequest dto) {
    if (!this.adminPassword.equals(adminPassword)) {
      throw new ForbiddenException("권한이 없습니다.");
    }
    Shelter shelter = Shelter.builder()
        .name(dto.getName())
        .password(passwordEncoder.encode(dto.getPassword()))
        .latitude(dto.getLatitude())
        .longitude(dto.getLongitude())
        .build();
    return shelterService.save(shelter);
  }
}
