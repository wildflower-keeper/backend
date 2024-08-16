package org.wildflowergardening.backend.core.wildflowergardening.domain.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class Session {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "token", length = 80, nullable = false)
  private String token;

  @Column(name = "user_role", nullable = false)
  @Enumerated(EnumType.STRING)
  private UserRole userRole;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "expired_at", nullable = false)
  @Setter
  private LocalDateTime expiredAt;

  @Column(name = "created_at", nullable = false)
  @Comment("생성일시")
  private LocalDateTime createdAt;
}
