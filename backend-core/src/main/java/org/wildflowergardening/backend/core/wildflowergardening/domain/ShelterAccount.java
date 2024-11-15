package org.wildflowergardening.backend.core.wildflowergardening.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class ShelterAccount {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shelterId", nullable = false)
    @Comment("보호소 id")
    private Long shelterId;

    @Column(name = "email", nullable = false, unique = true)
    @Comment("이메일")
    @Setter
    private String email;

    @Column(name = "password", nullable = false)
    @Comment("비밀번호")
    @Setter
    private String password;

    @Column(name = "name", nullable = false)
    @Comment("이름")
    private String name;

    @Column(name = "phone_number", unique = true)
    @Comment("전화번호")
    @Setter
    private String phoneNumber;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    @Comment("생성일시")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_updated_at", nullable = false)
    @Comment("마지막 수정일시")
    private LocalDateTime lastUpdatedAt;

    @Column(name = "deleted_at")
    @Comment("삭제일시")
    private LocalDateTime deletedAt;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private UserRole userRole;

    @Column(name = "remark")
    @Setter
    private String remark;
}
