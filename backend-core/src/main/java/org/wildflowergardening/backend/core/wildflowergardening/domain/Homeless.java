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
public class Homeless {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @Comment("노숙인 성함")
    @Setter
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id", nullable = false)
    @Comment("센터 id")
    private Shelter shelter;

    @Column(name = "device_id", nullable = true)
    @Comment("노숙인의 디바이스 id")
    @Setter
    private String deviceId;

    @Column(name = "room", nullable = false)
    @Comment("방번호")
    @Setter
    private String room;

    @Column(name = "birth_date", nullable = true)
    @Comment("생년월일")
    @Setter
    private LocalDate birthDate;

    @Column(name = "phone_number", nullable = true)
    @Comment("노숙인 휴대폰번호")
    @Setter
    private String phoneNumber;

    @Column(name = "memo")
    @Comment("메모")
    @Setter
    private String memo;

    @Column(name = "admission_date", nullable = true)
    @Comment("센터 입소일")
    @Setter
    private LocalDate admissionDate;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    @Comment("생성일시")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_updated_at", nullable = false)
    @Comment("마지막 수정일시")
    private LocalDateTime lastUpdatedAt;
}
