package org.wildflowergardening.backend.core.wildflowergardening.domain;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeTargetRepository extends JpaRepository<NoticeTarget, Long> {
    List<NoticeTarget> getNoticeTargetByNoticeIdAndHomelessId(Long noticeId, Long homelessId);
}
