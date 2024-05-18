package org.wildflowergardening.backend.core.wildflowergardening.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
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
@Entity(name = "outing")
@Table(name = "outing")
@EntityListeners(AuditingEntityListener.class)
public class Outing {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "outing_type", length = 20)
  @Enumerated(EnumType.STRING)
  @Comment("외출 외박 유형 (DAYTIME_OUTING:외출, SLEEPOVER:외박)")
  private Type outingType;

  @Enumerated
  @Column(name = "creator_type", nullable = false)
  @Comment("외출/외박을 신청한 계정 유형 (HOMELESS:노숙인계정, SHELTER_PUBLIC:센터노숙인공용계정, SHELTER:센터관리자계정)")
  private UserRole creatorType;

  @Column(name = "shelter_id", nullable = false)
  @Comment("센터 id")
  private Long shelterId;

  @Column(name = "homeless_id", nullable = true)
  @Comment("노숙인 id")
  private Long homelessId;

  @Column(name = "name", nullable = false)
  @Comment("노숙인 성함")
  private String homelessName;

  @Column(name = "phone_number", nullable = true)
  @Comment("노숙인 전화번호")
  private String phoneNumber;

  @Column(name = "start_date", nullable = false)
  @Comment("외박/외출 시작일")
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  @Comment("외박/외출 종료일")
  private LocalDate endDate;

  @CreatedDate
  @Column(name = "created_at", nullable = false)
  @Comment("생성일시")
  private LocalDateTime createdAt;

  @Column(name = "deleted_at", nullable = true)
  @Comment("삭제일시")
  private LocalDateTime deletedAt;

  public enum Type {
    DAYTIME_OUTING,    // 외출
    SLEEPOVER          // 외박
  }
}
