package org.wildflowergardening.backend.core.wildflowergardening.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wildflowergardening.backend.core.wildflowergardening.domain.dto.NoticeRecipientReadDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NoticeRecipientRepository extends JpaRepository<NoticeRecipient, Long> {
    Optional<NoticeRecipient> findByNoticeIdAndHomelessId(Long noticeId, Long homelessId);

    @Query(
            " select new org.wildflowergardening.backend.core.wildflowergardening.domain.dto.NoticeRecipientReadDto"
                    + "(nr.homelessId, nr.isRead) "
                    + " from NoticeRecipient nr "
                    + " where nr.noticeId = :noticeId " +
                    "and nr.shelterId = :shelterId")
    List<NoticeRecipientReadDto> findAllHomelessIdAndReadByNoticeId(@Param("noticeId") Long noticeId, @Param("shelterId") Long shelterId);

    @Query("SELECT nr FROM NoticeRecipient nr " +
            "WHERE nr.homelessId = :homelessId " +
            "AND nr.createdAt >= :startDate " +
            "AND nr.createdAt < :endDate " +
            "ORDER BY nr.createdAt DESC")
    List<NoticeRecipient> findRecentNoticeIds(@Param("homelessId") Long homelessId,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    List<NoticeRecipient> findAllByHomelessId(Long homelessId);

    @Query("SELECT count(*) from NoticeRecipient nr " +
            "WHERE nr.noticeId =:noticeId "
            + "AND nr.isRead = :isRead")
    Long findCountByNoticeIdAndReadStatus(@Param("noticeId") Long noticeId, @Param("isRead") boolean isRead);

    @Query("SELECT count(*) from NoticeRecipient nr " +
            "WHERE nr.noticeId =:noticeId")
    Long findAllCountByNoticeId(@Param("noticeId") Long noticeId);

    @Query("SELECT nr.homelessId FROM NoticeRecipient nr " +
            "WHERE nr.noticeId=:noticeId "
            + "AND nr.isRead = :isRead")
    List<Long> findHomelessIdByNoticeIdAndReadStatus(@Param("noticeId") Long noticeId, @Param("isRead") boolean isRead);

    @Query("SELECT nr.homelessId FROM NoticeRecipient nr " +
            "WHERE nr.noticeId=:noticeId "
            + "AND nr.participateStatus = :participateStatus")
    List<Long> findHomelessIdByNoticeIdAndParticipateStatus(@Param("noticeId") Long noticeId, @Param("participateStatus") ParticipateStatus participateStatus);

}
