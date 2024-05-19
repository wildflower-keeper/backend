package org.wildflowergardening.backend.api.wildflowergardening.application.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.wildflowergardening.backend.api.kernel.application.exception.ExceptionType;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum WildflowerExceptionType implements ExceptionType {
  SHELTER_LOGIN_ID_PASSWORD_INVALID("센터 ID 또는 Password가 올바르지 않습니다."),
  WILDFLOWER_ADMIN_AUTHENTICATION_FAILED("들꽃지기 관리자 인증에 실패했습니다."),
  ;

  private final String message;

  @Override
  public String code() {
    return this.name();
  }

  @Override
  public String message() {
    return this.message;
  }
}
