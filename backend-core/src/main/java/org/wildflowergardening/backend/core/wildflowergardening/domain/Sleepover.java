package org.wildflowergardening.backend.core.wildflowergardening.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class Sleepover {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated
  @Column(name = "creator_type", nullable = false)
  @Comment("외출/외박을 신청한 계정 유형 (HOMELESS:노숙인계정, SHELTER_PUBLIC:센터노숙인공용계정, SHELTER:센터관리자계정)")
  private UserRole creatorType;

  @Column(name = "shelter_id", nullable = false)
  @Comment("센터 id")
  private Long shelterId;

  @Column(nullable = true)
  @Comment("노숙인 id")
  private Long homelessId;

  @Column(nullable = false)
  @Comment("노숙인 이름")
  private String homelessName;

  @Column(nullable = false)
  @Comment("노숙인 연락처")
  private String homelessPhoneNumber;

  @Column(name = "start_date", nullable = false)
  @Comment("외박/외출 시작일")
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  @Comment("외박/외출 종료일")
  private LocalDate endDate;

  @Column(name = "reason", nullable = true)
  @Comment("외박사유")
  private String reason;

  @Column(name = "emergency_contact", nullable = true)
  @Comment("비상연락처")
  private String emergencyContact;

  @CreatedDate
  @Column(name = "created_at", nullable = false)
  @Comment("생성일시")
  private LocalDateTime createdAt;

  @Column(name = "deleted_at", nullable = true)
  @Comment("삭제일시")
  private LocalDateTime deletedAt;

  public static LocalDate calcMinStartDate(LocalDate now) {
    return now.minusDays(2);
  }

  public static LocalDate calcMaxEndDate(LocalDate now) {
    return now.plusDays(32);
  }

  public boolean cancelableAt(LocalDate targetDate) {
    return this.startDate.isBefore(targetDate.minusDays(1));
  }

  public void toSoftDeleted() {
    this.deletedAt = LocalDateTime.now();
  }
}
