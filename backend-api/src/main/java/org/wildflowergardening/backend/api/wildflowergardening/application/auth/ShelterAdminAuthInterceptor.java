package org.wildflowergardening.backend.api.wildflowergardening.application.auth;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.wildflowergardening.backend.core.wildflowergardening.application.SessionService;
import org.wildflowergardening.backend.core.wildflowergardening.application.UserContextHolder;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.ShelterUserContext;

@Component
@RequiredArgsConstructor
public class ShelterAdminAuthInterceptor implements HandlerInterceptor {

  public static final String AUTH_HEADER_NAME = "session-id";

  private final SessionService sessionService;
  private final UserContextHolder userContextHolder;

  /**
   * @return true: 다음 단계 진행 (요청 마저 처리), false: 다음단계 진행하지 않음 (요청 처리 종료)
   */
  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler
  ) {
    if (!(handler instanceof HandlerMethod handlerMethod)) {    // 언제 이런 경우가 되는지?
      return true;
    }
    ShelterAuthorized shelterAuthAnnotation = handlerMethod.getMethodAnnotation(
        ShelterAuthorized.class
    );
    // 센터 auth 불필요
    if (shelterAuthAnnotation == null) {
      return true;
    }
    // 센터 auth 필요 - session id header 검사
    String sessionId = request.getHeader(AUTH_HEADER_NAME);
    Optional<Session> sessionOptional = sessionService.getSession(sessionId, LocalDateTime.now());

    if (sessionOptional.isEmpty()) {
      response.setStatus(HttpStatus.FORBIDDEN.value());
      return false;
    }
    Session session = sessionOptional.get();
    userContextHolder.setUserContext(ShelterUserContext.builder()
        .shelterId(session.getUserId())
        .shelterName(session.getUsername())
        .build());
    return true;
  }
}
