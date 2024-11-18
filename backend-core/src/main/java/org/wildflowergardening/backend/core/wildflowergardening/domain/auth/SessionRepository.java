package org.wildflowergardening.backend.core.wildflowergardening.domain.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, String> {

    Optional<Session> findByToken(String token);

    Optional<Session> findByTokenAndUserRole(String token, UserRole role);

    List<Session> findAllByUserIdAndUserRole(Long userId, UserRole userRole);
}
