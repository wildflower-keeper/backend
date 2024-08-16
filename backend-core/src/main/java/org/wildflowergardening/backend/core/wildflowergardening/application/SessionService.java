package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.SessionRepository;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@Service
@RequiredArgsConstructor
public class SessionService {

  private final SessionRepository sessionRepository;

  @Transactional
  public Session save(Session session) {
    return sessionRepository.save(session);
  }

  @Transactional(readOnly = true)
  public Optional<Session> getSession(String sessionToken, LocalDateTime now) {
    return sessionRepository.findByToken(sessionToken)
        .filter(session -> session.getExpiredAt().isAfter(now));
  }

  @Transactional
  public void deleteAllBy(UserRole userRole, Long userId) {
    List<Session> sessions = sessionRepository.findAllByUserIdAndUserRole(userId, userRole);
    sessionRepository.deleteAll(sessions);
  }
}
