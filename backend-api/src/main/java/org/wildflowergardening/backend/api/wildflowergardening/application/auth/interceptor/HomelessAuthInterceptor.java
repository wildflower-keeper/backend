package org.wildflowergardening.backend.api.wildflowergardening.application.auth.interceptor;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.HomelessAppJwtProvider;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.UserContextHolder;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.annotation.HomelessAuthorized;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.HomelessUserContext;

@Component
@RequiredArgsConstructor
public class HomelessAuthInterceptor implements HandlerInterceptor {

  public static final String AUTH_HEADER_NAME = "auth-token";

  private final UserContextHolder userContextHolder;
  private final HomelessAppJwtProvider homelessAppJwtProvider;

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
    HomelessAuthorized homelessAuthorized = handlerMethod.getMethodAnnotation(
        HomelessAuthorized.class
    );
    // 노숙인 auth 불필요
    if (homelessAuthorized == null) {
      return true;
    }
    // 노숙인 auth 필요
    String jwt = request.getHeader(AUTH_HEADER_NAME);

    if (StringUtils.isEmpty(jwt)) {
      response.setStatus(HttpStatus.FORBIDDEN.value());
      return false;
    }
    try {
      HomelessUserContext userContext = homelessAppJwtProvider.getUserContextFrom(jwt);
      userContextHolder.setUserContext(userContext);
      return true;

    } catch (Exception e) {
      response.setStatus(HttpStatus.FORBIDDEN.value());
      return false;
    }
  }
}
