package org.wildflowergardening.backend.core.wildflowergardening.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class DailyHomelessCounts {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Comment("센터 id")
    private Long shelterId;

    @ColumnDefault("-1")
    @Comment("현재 가입된 homeless수")
    @Setter
    private Long count;

    @Column(nullable = false)
    @Comment("관측 날짜")
    private LocalDate recordedDate;

    @CreatedDate
    private LocalDateTime createdAt;
}
