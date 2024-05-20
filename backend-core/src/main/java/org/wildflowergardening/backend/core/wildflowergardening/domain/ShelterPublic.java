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
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/*
 * 센터 공용 디바이스 계정
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "shelter_public")
@Table(name = "shelter_public")
@EntityListeners(AuditingEntityListener.class)
public class ShelterPublic {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "shelter_id", nullable = false)
  @Comment("센터 id")
  private Shelter shelter;

  @Column(name = "device_id", nullable = false)
  @Comment("센터 공용 디바이스 id")
  private String deviceId;

  @Column(name = "device_name", nullable = false)
  @Comment("센터 공용 디바이스 별명")
  private String deviceName;

  @CreatedDate
  @Column(name = "created_at", nullable = false)
  @Comment("생성일시")
  private LocalDateTime createdAt;
}
