package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.VerificationCode;
import org.wildflowergardening.backend.core.wildflowergardening.domain.VerificationCodeRepository;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {
    private final VerificationCodeRepository verificationCodeRepository;

    //TODO: 인증 코드 생성하기
    @Transactional
    public Long create(Long shelterId, String code) {
        VerificationCode verificationCode = VerificationCode.builder()
                .shelterId(shelterId)
                .code(code)
                .build();

        return verificationCodeRepository.save(verificationCode).getId();
    }

}
