package org.wildflowergardening.backend.api.wildflowergardening.application.auth.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.UserContextHolder;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.annotation.ShelterAuthorized;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.ShelterUserContext;
import org.wildflowergardening.backend.core.wildflowergardening.application.SessionService;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.Session;

@Component
@RequiredArgsConstructor
public class ShelterAdminAuthInterceptor implements HandlerInterceptor {

  public static final String AUTH_HEADER_NAME = "session-token";

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
    // 센터 auth 필요
    String sessionToken = request.getHeader(AUTH_HEADER_NAME);
    Optional<Session> sessionOptional = sessionService.getSession(sessionToken, LocalDateTime.now());

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
