package org.wildflowergardening.backend.api.wildflowergardening.application.auth.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.UserContextHolder;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.annotation.ShelterAuthorized;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.ShelterUserContext;
import org.wildflowergardening.backend.core.wildflowergardening.application.SessionService;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@Component
@RequiredArgsConstructor
public class ShelterAuthInterceptor implements HandlerInterceptor {

    public static final String AUTH_HEADER_NAME = "auth-token";

    private final SessionService sessionService;
    private final UserContextHolder userContextHolder;

    /**
     * @return true: 다음 단계 진행 (요청 마저 처리), false: 다음단계 진행하지 않음 (요청 처리 종료)
     */
    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler
    ) {
    /*
     ShelterAuthorized 어노테이션이 있으면 권한 평가 실행
     */
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        ShelterAuthorized shelterAuthAnnotation = handlerMethod.getMethodAnnotation(
                ShelterAuthorized.class
        );
        if (shelterAuthAnnotation == null) {
            return true;
        }
        String sessionToken = request.getHeader(AUTH_HEADER_NAME);
        Optional<Session> sessionOptional = sessionService.getSession(sessionToken, LocalDateTime.now());

        if (sessionOptional.isEmpty()) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        Session session = sessionOptional.get();
        session.setExpiredAt(LocalDateTime.now().plusMinutes(30));    // 세션 연장

        userContextHolder.setUserContext(ShelterUserContext.builder()
                .role(session.getUserRole())
                .shelterId(session.getUserId())
                .userName(session.getUsername())
                .build());
        return true;
    }
}
