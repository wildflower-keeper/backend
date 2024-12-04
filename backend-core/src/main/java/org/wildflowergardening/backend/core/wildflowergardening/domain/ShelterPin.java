package org.wildflowergardening.backend.core.wildflowergardening.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class ShelterPin {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  @Comment("센터 id")
  private Long shelterId;

  @Column(nullable = false, length = 4)
  @Comment("네자리 숫자 pin 번호")
  @Setter
  private String pin;

  @CreatedDate
  @Column(name = "created_at", nullable = false)
  @Comment("생성일시")
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "last_updated_at", nullable = false)
  @Comment("마지막 수정일시")
  private LocalDateTime lastUpdatedAt;

  public static String generatePin() {
    int randomNum = new SecureRandom().nextInt(10_000);
    return String.format("%04d", randomNum);
  }

  public LocalDateTime calcExpiredAt() {
    return lastUpdatedAt.toLocalDate().plusDays(14).atStartOfDay();
  }

  public boolean isExpired() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expiredAt = calcExpiredAt();
    return now.isAfter(expiredAt) || now.isEqual(expiredAt);
  }
}
