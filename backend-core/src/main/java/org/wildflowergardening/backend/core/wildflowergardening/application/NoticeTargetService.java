package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NoticeRecipient;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NoticeRecipientRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeTargetService {

    private final NoticeRecipientRepository noticeRecipientRepository;

    @Transactional
    public Long save(NoticeRecipient noticeRecipient) {
        return noticeRecipientRepository.save(noticeRecipient).getId();
    }

    @Transactional
    public void updateReadStatus(Long noticeId, Long homelessId, boolean status) {
        List<NoticeRecipient> noticeRecipientList = noticeRecipientRepository.getNoticeRecipientByNoticeIdAndHomelessId(noticeId, homelessId);
        for (NoticeRecipient noticeRecipient : noticeRecipientList) {
            noticeRecipient.setReadStatus(status);
            noticeRecipient.setReadAt(LocalDateTime.now());
        }
    }
}
