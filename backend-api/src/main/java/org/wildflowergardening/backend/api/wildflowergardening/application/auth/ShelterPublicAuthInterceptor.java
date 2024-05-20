package org.wildflowergardening.backend.api.wildflowergardening.application.auth;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterPublicService;
import org.wildflowergardening.backend.core.wildflowergardening.application.UserContextHolder;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ShelterPublic;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.ShelterPublicUserContext;

@Component
@RequiredArgsConstructor
public class ShelterPublicAuthInterceptor implements HandlerInterceptor {

  public static final String AUTH_HEADER_NAME = "device-id";

  private final ShelterPublicService homelessService;
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
    ShelterPublicAuthorized shelterPublicAuthorized = handlerMethod.getMethodAnnotation(
        ShelterPublicAuthorized.class
    );
    // ShelterPublic auth 불필요
    if (shelterPublicAuthorized == null) {
      return true;
    }
    // ShelterPublic 필요 - device id header 검사
    String deviceId = request.getHeader(AUTH_HEADER_NAME);

    if (StringUtils.isEmpty(deviceId)) {
      response.setStatus(HttpStatus.FORBIDDEN.value());
      return false;
    }
    Optional<ShelterPublic> shelterPublicOptional = homelessService.getOneByDeviceId(deviceId);

    if (shelterPublicOptional.isEmpty()) {
      response.setStatus(HttpStatus.FORBIDDEN.value());
      return false;
    }
    ShelterPublic shelterPublic = shelterPublicOptional.get();
    userContextHolder.setUserContext(
        ShelterPublicUserContext.from(shelterPublic)
    );
    return true;
  }
}
