package org.wildflowergardening.backend.core.wildflowergardening.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    // TODO : sheterID를 기반으로 code가져오기 그런데 마감 기한이 현재 날짜보다 긴 것으로 그리고 사용되지 않은 것으로 그리고 최근 생성된 순으로
    @Query("SELECT v FROM VerificationCode v " +
            "WHERE v.email = :email " +
            "AND v.expiredAt > :currentDate " +
            "AND v.isUsed = false " +
            "ORDER BY v.createdAt DESC " +
            "limit 1")
    Optional<VerificationCode> findFirstValidCodeByEmail(
            @Param("email") String email,
            @Param("currentDate") LocalDateTime currentDate
    );

    @Query("SELECT v.id FROM VerificationCode v WHERE v.expiredAt < :currentTime OR v.isUsed = true")
    List<Long> findExpiredOrUsedIds(@Param("currentTime") LocalDateTime currentTime);

}
