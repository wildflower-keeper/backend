package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.ShelterIdPasswordDto;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.SessionRepository;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ShelterRepository;
import org.wildflowergardening.backend.core.wildflowergardening.domain.UserRole;

@RequiredArgsConstructor
@Service
public class ShelterService {

  private final ShelterRepository shelterRepository;
  private final SessionRepository sessionRepository;

  @Transactional
  public Long save(Shelter shelter) {
    return shelterRepository.save(shelter).getId();
  }

  @Transactional(readOnly = true)
  public List<Shelter> getAll() {
    return shelterRepository.findAll();
  }

  @Transactional
  public Session authenticate(ShelterIdPasswordDto dto) {
    Optional<Shelter> authenticatedShelter =
        shelterRepository.findByIdAndPassword(dto.getShelterId(), dto.getPassword());

    if (authenticatedShelter.isEmpty()) {
      throw new IllegalArgumentException("센터 로그인 실패 - 로그인 정보가 올바르지 않습니다.");
    }
    Shelter shelter = authenticatedShelter.get();
    LocalDateTime now = LocalDateTime.now();

    return sessionRepository.save(Session.builder()
        .userRole(UserRole.SHELTER)
        .userId(shelter.getId())
        .username(shelter.getName())
        .createdAt(now)
        .expiredAt(now.plusHours(1))
        .build());
  }
}
