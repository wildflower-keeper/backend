package org.wildflowergardening.backend.api.wildflowergardening.application;

import static org.wildflowergardening.backend.api.wildflowergardening.application.exception.WildflowerExceptionType.SHELTER_LOGIN_ID_PASSWORD_INVALID;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.api.kernel.application.exception.ApplicationLogicException;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.SessionResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterLoginRequest;
import org.wildflowergardening.backend.core.wildflowergardening.application.SessionService;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterService;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@Service
@RequiredArgsConstructor
public class ShelterAdminAppService {

  private final ShelterService shelterService;
  private final SessionService sessionService;
  private final PasswordEncoder passwordEncoder;

  public SessionResponse login(ShelterLoginRequest dto) {
    Optional<Shelter> shelterOptional = shelterService.getShelterById(dto.getId());

    if (shelterOptional.isEmpty() || !passwordEncoder.matches(
        dto.getPw(), shelterOptional.get().getPassword()
    )) {
      throw new ApplicationLogicException(SHELTER_LOGIN_ID_PASSWORD_INVALID);
    }

    Shelter shelter = shelterOptional.get();
    LocalDateTime now = LocalDateTime.now();

    Session session = sessionService.save(Session.builder()
        .uuid(UUID.randomUUID().toString())
        .userRole(UserRole.SHELTER)
        .userId(shelter.getId())
        .username(shelter.getName())
        .createdAt(now)
        .expiredAt(now.plusHours(1))
        .build());

    return SessionResponse.builder()
        .sessionId(session.getUuid())
        .expiredAt(session.getExpiredAt())
        .build();
  }
}
