package org.wildflowergardening.backend.core.wildflowergardening.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wildflowergardening.backend.core.wildflowergardening.domain.dto.NoticeRecipientReadDto;

import java.util.List;

public interface NoticeRecipientRepository extends JpaRepository<NoticeRecipient, Long> {
    List<NoticeRecipient> findByNoticeIdAndHomelessId(Long noticeId, Long homelessId);

    @Query(
            " select new org.wildflowergardening.backend.core.wildflowergardening.domain.dto.NoticeRecipientReadDto"
                    + "(nr.homelessId, nr.isRead) "
                    + " from NoticeRecipient nr "
                    + " where nr.noticeId = :noticeId " +
                    "and nr.shelterId = :shelterId")
    List<NoticeRecipientReadDto> findAllHomelessIdAndReadByNoticeId(@Param("noticeId") Long noticeId, @Param("shelterId") Long shelterId);
}
