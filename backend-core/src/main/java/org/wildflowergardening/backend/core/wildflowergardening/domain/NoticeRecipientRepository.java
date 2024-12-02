package org.wildflowergardening.backend.core.wildflowergardening.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NoticeRecipientRepository extends JpaRepository<NoticeRecipient, Long> {
    List<NoticeRecipient> findByNoticeIdAndHomelessId(Long noticeId, Long homelessId);

    List<NoticeRecipient> findByNoticeIdAndShelterId(Long noticeId, Long shelterId);

    @Query("SELECT nr.noticeId FROM NoticeRecipient nr " +
            "WHERE nr.homelessId = :homelessId " +
            "AND nr.createdAt >= :startDate " +
            "AND nr.createdAt < :endDate " +
            "ORDER BY nr.createdAt DESC")
    List<Long> findRecentNoticeIds(@Param("homelessId") Long homelessId,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);
}
