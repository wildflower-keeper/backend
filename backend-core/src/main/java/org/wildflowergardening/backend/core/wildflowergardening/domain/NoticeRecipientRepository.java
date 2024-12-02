package org.wildflowergardening.backend.core.wildflowergardening.domain;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRecipientRepository extends JpaRepository<NoticeRecipient, Long> {
    List<NoticeRecipient> findByNoticeIdAndHomelessId(Long noticeId, Long homelessId);

    List<NoticeRecipient> findByNoticeIdAndShelterId(Long noticeId, Long shelterId);
}
