package org.wildflowergardening.backend.core.wildflowergardening.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface NoticeRecipientRepository extends JpaRepository<NoticeRecipient, Long> {
    List<NoticeRecipient> getNoticeRecipientByNoticeIdAndHomelessId(Long noticeId, Long homelessId);

    @Query("select nr.noticeId from NoticeRecipient nr where nr.homelessId = :homelessId order by nr.createdAt desc ")
    List<Long> getNoticeIdByHomelessId(Long homelessId);
}
