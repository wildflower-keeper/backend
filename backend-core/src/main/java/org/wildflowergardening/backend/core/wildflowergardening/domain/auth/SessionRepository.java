package org.wildflowergardening.backend.core.wildflowergardening.domain.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
