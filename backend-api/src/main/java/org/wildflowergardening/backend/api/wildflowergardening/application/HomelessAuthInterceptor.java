package org.wildflowergardening.backend.api.wildflowergardening.application;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessService;
import org.wildflowergardening.backend.core.wildflowergardening.application.UserContextHolder;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.UserContext;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.UserRole;

@Component
@RequiredArgsConstructor
public class HomelessAuthInterceptor implements HandlerInterceptor {

  private final HomelessService homelessService;
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
    HomelessAuthorized homelessAuthorized = handlerMethod.getMethodAnnotation(
        HomelessAuthorized.class
    );
    // 노숙인 auth 불필요
    if (homelessAuthorized == null) {
      return true;
    }
    // 노숙인 auth 필요 - device id header 검사
    String deviceId = request.getHeader("device-id");

    if (StringUtils.isEmpty(deviceId)) {
      response.setStatus(HttpStatus.FORBIDDEN.value());
      return false;
    }
    Optional<Homeless> homelessOptional = homelessService.getOneByDeviceId(deviceId);

    if (homelessOptional.isEmpty()) {
      response.setStatus(HttpStatus.FORBIDDEN.value());
      return false;
    }
    Homeless homeless = homelessOptional.get();
    userContextHolder.setUserContext(UserContext.builder()
        .userRole(UserRole.HOMELESS)
        .userId(homeless.getId())
        .username(homeless.getName())
        .build());
    return true;
  }
}
