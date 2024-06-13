package org.wildflowergardening.backend.core.wildflowergardening.domain.auth;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, String> {

  Optional<Session> findByToken(String token);
}
