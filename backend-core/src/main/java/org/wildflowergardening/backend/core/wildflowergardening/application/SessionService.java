package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.SessionRepository;

@Service
@RequiredArgsConstructor
public class SessionService {

  private final SessionRepository sessionRepository;

  @Transactional
  public Session save(Session session) {
    return sessionRepository.save(session);
  }

  @Transactional(readOnly = true)
  public Optional<Session> getSession(String sessionUuid) {
    return sessionRepository.findById(sessionUuid);
  }
}
