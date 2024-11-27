package org.wildflowergardening.backend.core.wildflowergardening.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class Notice {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shelter_id", nullable = false)
    @Comment("공지 사항을 작성 한 보호소")
    private Long shelterId;

    @Column(name = "shelter_account_id", nullable = false)
    @Comment("공지 사항을 작성 한 보호소 관리자 ID")
    private Long shelterAccountId;
    
    @Column(name = "title", nullable = false)
    @Comment("공지 사항 제목")
    private String title;

    @Column(name = "contents", nullable = false)
    @Comment("공지 사항 내용")
    private String contents;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    @Comment("생성일시")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_updated_at", nullable = false)
    @Comment("마지막 수정일시")
    private LocalDateTime lastUpdatedAt;
}
