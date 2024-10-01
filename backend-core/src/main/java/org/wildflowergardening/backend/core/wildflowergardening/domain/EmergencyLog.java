package org.wildflowergardening.backend.core.wildflowergardening.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class EmergencyLog {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    @Comment("생성일시")
    private LocalDateTime createdAt;

    @Column(name = "latitude", columnDefinition = "decimal(10,8)")
    @Comment("위도")
    private BigDecimal latitude;

    @Column(name = "longitude", columnDefinition = "decimal(11,8)")
    @Comment("경도")
    private BigDecimal longitude;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "homeless_id", nullable = false)
    @Comment("노숙인 id")
    private Homeless homless;

    @Column(name = "shelter_id", nullable = false)
    @Comment("보호소 id")
    private Long shelterId;


}
