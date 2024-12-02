package org.wildflowergardening.backend.core.wildflowergardening.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wildflowergardening.backend.core.wildflowergardening.domain.dto.NoticeRecipientReadDto;

import java.time.LocalDateTime;
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
