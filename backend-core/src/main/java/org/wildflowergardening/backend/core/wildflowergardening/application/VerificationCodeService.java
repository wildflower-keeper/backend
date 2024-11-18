package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.VerificationCode;
import org.wildflowergardening.backend.core.wildflowergardening.domain.VerificationCodeRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {
    private final VerificationCodeRepository verificationCodeRepository;
    private static final int VALIDATION_TIME = 10;

    @Transactional
    public Long create(String email, String code) {
        LocalDateTime curTime = LocalDateTime.now();
        VerificationCode verificationCode = VerificationCode.builder()
                .email(email)
                .code(code)
                .expiredAt(curTime.plusMinutes(VALIDATION_TIME))
                .build();

        return verificationCodeRepository.save(verificationCode).getId();
    }

    @Transactional(readOnly = true)
    public Optional<VerificationCode> checkCode(String email) {
        LocalDateTime curTime = LocalDateTime.now();
        return verificationCodeRepository.findFirstValidCodeByEmail(email, curTime);
    }

}
