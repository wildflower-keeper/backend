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
import java.math.BigDecimal;
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

/**
 * 노숙인의 위치 상태가 바뀔 때 새 엔티티가 생성됨
 * 위치 상태가 달라지지 않은 경우 마지막위도, 마지막경도, 마지막 위치 확인 일시만 변경됨
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class LocationTracking {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "homeless_id", nullable = false)
  @Comment("노숙인 id")
  private Long homelessId;

  @Column(name = "shelter_id", nullable = false)
  @Comment("센터 id")
  private Long shelterId;

  @Enumerated(EnumType.STRING)
  @Column(name = "location_status", nullable = false)
  @Comment("위치 상태")
  @Setter
  private LocationStatus locationStatus;

  @Setter
  @Column(name = "last_latitude", columnDefinition = "decimal(10,8)", nullable = true)
  @Comment("마지막 위도")
  private BigDecimal lastLatitude;

  @Setter
  @Column(name = "last_longitude", columnDefinition = "decimal(11,8)", nullable = true)
  @Comment("마지막 경도")
  private BigDecimal lastLongitude;

  @CreatedDate
  @Column(name = "first_tracked_at", nullable = false)
  @Comment("최초 위치 확인 일시 (=생성일시)")
  private LocalDateTime firstTrackedAt;

  @Setter
  @LastModifiedDate
  @Column(name = "last_tracked_at", nullable = false)
  @Comment("마지막 위치 확인 일시")
  private LocalDateTime lastTrackedAt;
}
