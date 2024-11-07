package org.wildflowergardening.backend.core.kernel.application.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum WildflowerExceptionType implements ExceptionType {
    SHELTER_ADMIN_LOGIN_ID_PASSWORD_INVALID("센터 ID 또는 Password 가 올바르지 않습니다."),
    SHELTER_ADMIN_LOGIN_EMAIL_INVALID("등록되지 않은 email입니다"),
    SHELTER_ADMIN_LOGIN_CODE_INVALID("이메일 인증 코드가 유효하지 않습니다"),
    SHELTER_ADMIN_CHIEF_OFFICERS_TOO_MANY("책임자 수가 많아서 더이상 생성할 수 없습니다."),
    SHELTER_ADMIN_CHIEF_OFFICER_PHONE_NUMBER_ALREADY_EXISTS("해당 전화번호로 책임자 정보가 이미 존재합니다."),
    SHELTER_ADMIN_CHIEF_OFFICER_NOT_FOUND("책임자 정보가 존재하지 않습니다."),
    HOMELESS_APP_CREATE_ACCOUNT_SHELTER_ID_PIN_INVALID("센터 ID 또는 PIN 번호가 올바르지 않습니다."),
    HOMELESS_APP_ESSENTIAL_TERMS_NOT_AGREED("필수 약관이 동의되지 않았습니다."),
    HOMELESS_APP_NOT_DATA_LOCATION("위치 정보가 존재하지 않습니다."),
    WILDFLOWER_ADMIN_AUTHENTICATION_FAILED("들꽃지기 관리자 인증에 실패했습니다."),
    HOMELESS_APP_CREATE_DEFAULT_LOCATION("기본 위치 상태 생성 실패");;

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
