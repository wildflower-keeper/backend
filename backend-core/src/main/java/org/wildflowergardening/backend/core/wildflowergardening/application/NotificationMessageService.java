package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NotificationMessage;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NotificationMessageRepository;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NotificationMessageType;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationMessageService {
    private final NotificationMessageRepository notificationMessageRepository;

    @Transactional(readOnly = true)
    public Optional<NotificationMessage> getMessageByType(NotificationMessageType type) {
        return notificationMessageRepository.findMessageByType(type);
    }
}
