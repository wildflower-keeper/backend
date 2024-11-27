package org.wildflowergardening.backend.core.wildflowergardening;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NoticeRecipient;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NoticeRecipientRepository;

@Service
@RequiredArgsConstructor
public class NoticeTargetService {

    private final NoticeRecipientRepository noticeRecipientRepository;

    @Transactional
    public Long save(NoticeRecipient noticeRecipient) {

        return noticeRecipientRepository.save(noticeRecipient).getId();
    }
}
