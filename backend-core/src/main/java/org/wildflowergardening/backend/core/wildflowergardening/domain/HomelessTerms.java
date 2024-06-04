package org.wildflowergardening.backend.core.wildflowergardening.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class HomelessTerms {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title", nullable = false)
  @Comment("약관명")
  private String title;

  @Lob
  @Column(name = "detail", nullable = false)
  @Comment("약관 내용")
  private String detail;

  @Column(name = "is_essential", nullable = false)
  @Comment("필수 여부")
  private Boolean isEssential;

  @Column(name = "start_date", nullable = false)
  @Comment("적용 시작일")
  private LocalDate startDate;

  @Column(name = "deprecated_date", nullable = true)
  @Comment("더이상 적용되지 않게되는 시작일")
  private LocalDate deprecatedDate;

  @CreatedDate
  @Column(name = "created_at", nullable = false)
  @Comment("생성일시")
  private LocalDateTime createdAt;
}
