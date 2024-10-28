package org.wildflowergardening.backend.core.wildflowergardening.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    // TODO : sheterID를 기반으로 code가져오기 그런데 마감 기한이 현재 날짜보다 긴 것으로 그리고 사용되지 않은 것으로

}
