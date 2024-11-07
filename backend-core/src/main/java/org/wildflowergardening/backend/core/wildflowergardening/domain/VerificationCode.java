package org.wildflowergardening.backend.core.wildflowergardening.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class VerificationCode {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    @Comment("인증 번호를 받은 이메일")
    private String email;

    @Column(name = "expired_at", nullable = false)
    @Comment("만료 시간")
    private LocalDateTime expiredAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    @Comment("생성일시")
    private LocalDateTime createdAt;

    @Column(name = "code", nullable = false)
    @Comment("인증 번호")
    private String code;

    @Column(name = "is_used", nullable = false)
    @Builder.Default
    @Setter
    private boolean isUsed = false;
}
