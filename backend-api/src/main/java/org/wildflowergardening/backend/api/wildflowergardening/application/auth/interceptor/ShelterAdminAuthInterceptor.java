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
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.annotation.ShelterAdminAuthorized;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.ShelterUserContext;
import org.wildflowergardening.backend.core.wildflowergardening.application.SessionService;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@Component
@RequiredArgsConstructor
public class ShelterAdminAuthInterceptor implements HandlerInterceptor {

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
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        ShelterAdminAuthorized shelterAuthAnnotation = handlerMethod.getMethodAnnotation(
                ShelterAdminAuthorized.class
        );
        if (shelterAuthAnnotation == null) {
            return true;
        }
        String sessionToken = request.getHeader(AUTH_HEADER_NAME);
        Optional<Session> sessionOptional = sessionService.getAdminSession(sessionToken, LocalDateTime.now(), UserRole.SHELTER_ADMIN);

        if (sessionOptional.isEmpty()) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        Session session = sessionOptional.get();

        userContextHolder.setUserContext(ShelterUserContext.builder()
                .role(session.getUserRole())
                .shelterId(session.getShelterId())
                .userId(session.getUserId())
                .userName(session.getUsername())
                .build());
        return true;
    }
}
