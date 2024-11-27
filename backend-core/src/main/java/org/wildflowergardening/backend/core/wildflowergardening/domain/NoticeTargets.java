package org.wildflowergardening.backend.core.wildflowergardening.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

public class NoticeTargets {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shelter_id", nullable = false)
    @Comment("공지 사항을 작성 한 보호소 id")
    private Long shelterId;

    @Column(name = "notice_id", nullable = false)
    @Comment("공지사항 id")
    private Long noticeId;

    @Column(name = "homeless_id", nullable = false)
    @Comment("노숙인 id")
    private Long homelessId;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    @Setter
    private boolean isRead = false;

    @Column(name = "read_at")
    @Comment("읽은 시간")
    @Setter
    private LocalDateTime readAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    @Comment("생성일시")
    private LocalDateTime createdAt;
}
