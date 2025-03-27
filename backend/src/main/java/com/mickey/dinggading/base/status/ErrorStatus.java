package com.mickey.dinggading.base.status;

import com.mickey.dinggading.base.code.BaseErrorCode;
import com.mickey.dinggading.base.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 일반적인 응답
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    // 회원 관련 오류
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4001", "존재하지 않는 계정입니다."),

    // 랭크 관련 오류
    RANK_NOT_FOUND(HttpStatus.NOT_FOUND, "RANK4001", "랭크 정보를 찾을 수 없습니다."),

    // 입력값 관련 오류
    INVALID_INSTRUMENT_TYPE(HttpStatus.BAD_REQUEST, "INST4001", "유효하지 않은 악기 타입입니다."),

    // 토큰 관련 에러
    TOKEN_EXPIRED_ERROR(HttpStatus.UNAUTHORIZED, "TOKEN4001", "토큰의 유효기간이 만료되었습니다."),
    TOKEN_MALFORMED_ERROR(HttpStatus.UNAUTHORIZED, "TOKEN4002", "토큰이 변형되었습니다."),
    TOKEN_UNSUPPORTED_ERROR(HttpStatus.BAD_REQUEST, "TOKEN4003", "지원되지 않는 형식의 토큰입니다."),
    TOKEN_SIGNATURE_ERROR(HttpStatus.UNAUTHORIZED, "TOKEN4004", "토큰의 서명이 유효하지 않습니다."),
    TOKEN_ILLEGAL_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "TOKEN4005", "토큰 값이 잘못되었습니다."),
    TOKEN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "TOKEN5000", "토큰 처리 중 알 수 없는 에러가 발생했습니다."),
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "TOKEN4006", "유저의 토큰정보를 얻어올 수 없습니다. 헤더를 확인하세요");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
