package com.mickey.dinggading.base.status;

import com.mickey.dinggading.base.code.BaseErrorCode;
import com.mickey.dinggading.base.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // Common Errors
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 내부 오류가 발생했습니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "접근 권한이 없습니다."),
    _METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON405", "지원하지 않는 HTTP 메서드입니다."),

    // RankMatching Errors
    RANK_MATCHING_NOT_FOUND(HttpStatus.NOT_FOUND, "RANKM4001", "존재하지 않는 랭크 매칭입니다."),
    ONGOING_MATCHING_EXISTS(HttpStatus.BAD_REQUEST, "RANKM4002", "이미 진행 중인 랭크 매칭이 있습니다."),
    ALREADY_RANKED(HttpStatus.BAD_REQUEST, "RANKM4003", "이미 티어가 배정되어 있습니다."),
    NEED_PLACEMENT_FIRST(HttpStatus.BAD_REQUEST, "RANKM4004", "먼저 배치고사를 진행해야 합니다."),
    NOT_IN_DEFENCE_PERIOD(HttpStatus.BAD_REQUEST, "RANKM4005", "현재 방어전 기간이 아닙니다."),
    INVALID_TARGET_TIER(HttpStatus.BAD_REQUEST, "RANKM4006", "유효하지 않은 목표 티어입니다."),
    TARGET_TIER_REQUIRED(HttpStatus.BAD_REQUEST, "RANKM4007", "목표 티어를 지정해야 합니다."),
    MAX_ATTEMPT_REACHED(HttpStatus.BAD_REQUEST, "RANKM4008", "최대 도전 횟수를 초과했습니다."),

    // SongPack Errors
    SONG_PACK_NOT_FOUND(HttpStatus.NOT_FOUND, "SONGP4001", "존재하지 않는 곡 팩입니다."),

    // Song Errors
    SONG_NOT_FOUND(HttpStatus.NOT_FOUND, "SONG4001", "존재하지 않는 곡입니다."),
    SONG_BY_INSTRUMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "SONG4002", "해당 악기의 곡이 존재하지 않습니다."),

    // Attempt Errors
    ATTEMPT_NOT_FOUND(HttpStatus.NOT_FOUND, "ATTEMPT4001", "존재하지 않는 시도 기록입니다."),

    // MemberRank Errors
    MEMBER_RANK_NOT_FOUND(HttpStatus.NOT_FOUND, "MRANK4001", "해당 회원의 랭크 정보가 존재하지 않습니다."),

    // Analysis Errors
    ANALYSIS_IN_PROGRESS(HttpStatus.ACCEPTED, "ANALYSIS2001", "분석이 진행 중입니다."),
    ANALYSIS_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "ANALYSIS5001", "음성 분석에 실패했습니다."),

    // 일반적인 응답
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),

    // 회원 관련 오류
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4001", "존재하지 않는 계정입니다."),
    // 일반적인 응답
    INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON402", "요청 매개변수가 올바르지 않습니다."),

    // 랭크 관련 오류
    RANK_NOT_FOUND(HttpStatus.NOT_FOUND, "RANK4001", "랭크 정보를 찾을 수 없습니다."),
    // 녹음 API 관련 오류 상태
    RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "RECORD4001", "녹음 정보를 찾을 수 없습니다."),
    INVALID_AUDIO_FILE(HttpStatus.BAD_REQUEST, "RECORD4002", "유효하지 않은 오디오 파일입니다."),

    // 입력값 관련 오류
    INVALID_INSTRUMENT_TYPE(HttpStatus.BAD_REQUEST, "INST4001", "유효하지 않은 악기 타입입니다."),

    // 밴드 관련 에러
    BAND_NOT_FOUND(HttpStatus.NOT_FOUND, "BAND4001", "존재하지 않는 밴드입니다"),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "BAND4002", "관리자 권한이 없는 계정입니다."),
    MEMBER_NOT_IN_BAND(HttpStatus.NOT_FOUND, "BAND4003", "해당 멤버가 밴드에 속해있지 않습니다."),
    BAND_MASTER_CANNOT_LEAVE(HttpStatus.BAD_REQUEST, "BAND4004", "밴드 마스터는 밴드를 떠날 수 없습니다. 먼저 다른 멤버에게 마스터 권한을 양도하세요."),
    CONTACT_NOT_FOUND(HttpStatus.NOT_FOUND, "BAND4005", "존재하지 않는 SNS 입니다."),
    INVALID_TRANSFER_REQUEST(HttpStatus.BAD_REQUEST, "BAND4006", "잘못된 권한 이전 요청입니다."),
    MEMBER_ALREADY_IN_BAND(HttpStatus.FOUND, "BAND4007", "이미 밴드에 가입된 멤버입니다."),

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
