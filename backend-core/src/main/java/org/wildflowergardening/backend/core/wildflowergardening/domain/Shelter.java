package org.wildflowergardening.backend.core.wildflowergardening.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "shelter")
@Table(name = "shelter")
@EntityListeners(AuditingEntityListener.class)
public class Shelter {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  @Comment("센터명")
  private String name;

  // Todo encrypt
  @Column(name = "password", nullable = false)
  @Comment("비밀번호")
  private String password;

  @Column(name = "latitude", columnDefinition = "decimal(10,8)", nullable = false)
  @Comment("위도")
  private BigDecimal latitude;

  @Column(name = "longitude", columnDefinition = "decimal(11,8)", nullable = false)
  @Comment("경도")
  private BigDecimal longitude;

  @CreatedDate
  @Column(name = "created_at", nullable = false)
  @Comment("생성일시")
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "last_updated_at", nullable = false)
  @Comment("마지막 수정일시")
  private LocalDateTime lastUpdatedAt;
}
