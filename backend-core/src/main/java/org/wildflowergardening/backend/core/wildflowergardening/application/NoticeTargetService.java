package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NoticeTarget;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NoticeTargetRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeTargetService {

    private final NoticeTargetRepository noticeTargetRepository;

    @Transactional
    public Long save(NoticeTarget noticeTarget) {
        return noticeTargetRepository.save(noticeTarget).getId();
    }

    @Transactional
    public void updateReadStatus(Long noticeId, Long homelessId, boolean status) {
        List<NoticeTarget> noticeTargetList = noticeTargetRepository.getNoticeTargetByNoticeIdAndHomelessId(noticeId, homelessId);
        for (NoticeTarget noticeTarget : noticeTargetList) {
            noticeTarget.setReadStatus(status);
            noticeTarget.setReadAt(LocalDateTime.now());
        }
    }
}
