package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.SessionRepository;

@Service
@RequiredArgsConstructor
public class SessionService {

  private final SessionRepository sessionRepository;

  @Transactional
  public Session save(Session session) {
    return sessionRepository.save(session);
  }

  @Transactional(readOnly = true)
  public Optional<Session> getSession(Long sessionId) {
    return sessionRepository.findById(sessionId);
  }
}
