package org.wildflowergardening.backend.core.wildflowergardening.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NotificationMessageRepository extends JpaRepository<NotificationMessage, Long> {

    Optional<NotificationMessage> findMessageByType(@Param("type") NotificationMessageType type);
}
