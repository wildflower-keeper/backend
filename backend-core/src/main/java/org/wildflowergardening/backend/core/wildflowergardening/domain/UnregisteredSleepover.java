package org.wildflowergardening.backend.core.wildflowergardening.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
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
public class UnregisteredSleepover {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "homeless_id", nullable = false)
  @Comment("노숙인 id")
  private Homeless homeless;

  @Column(name = "shelter_id", nullable = false)
  @Comment("센터 id")
  private Long shelterId;

  @Column(name = "target_date", nullable = false)
  @Comment("외박신청 없이 외박한 날")
  private LocalDate targetDate;

  @Column(name = "first_tracked_at", nullable = false)
  @Comment("미신청 외박 첫 확인 일시")
  private LocalDateTime firstTrackedAt;

  @Setter
  @Column(name = "last_tracked_at", nullable = false)
  @Comment("미신청 외박 마지막 확인 일시")
  private LocalDateTime lastTrackedAt;

  @CreatedDate
  @Column(name = "created_at", nullable = false)
  @Comment("생성일시")
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "last_updated_at", nullable = false)
  @Comment("마지막 수정일시")
  private LocalDateTime lastUpdatedAt;
}
