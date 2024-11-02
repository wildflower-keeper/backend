package org.wildflowergardening.backend.core.wildflowergardening.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    // TODO : sheterID를 기반으로 code가져오기 그런데 마감 기한이 현재 날짜보다 긴 것으로 그리고 사용되지 않은 것으로 그리고 최근 생성된 순으로
    @Query("SELECT v FROM VerificationCode v " +
            "WHERE v.shelterId = :shelterId " +
            "AND v.expiredAt > :currentDate " +
            "AND v.isUsed = false " +
            "ORDER BY v.createdAt DESC " +
            "limit 1")
    Optional<VerificationCode> findFirstValidCodeByShelterId(
            @Param("shelterId") Long shelterId,
            @Param("currentDate") LocalDateTime currentDate
    );

}
