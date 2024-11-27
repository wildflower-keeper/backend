package org.wildflowergardening.backend.core.wildflowergardening;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NoticeTarget;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NoticeTargetRepository;

@Service
@RequiredArgsConstructor
public class NoticeTargetService {

    private final NoticeTargetRepository noticeTargetRepository;

    @Transactional
    public Long save(NoticeTarget noticeTarget) {

        return noticeTargetRepository.save(noticeTarget).getId();
    }
}
