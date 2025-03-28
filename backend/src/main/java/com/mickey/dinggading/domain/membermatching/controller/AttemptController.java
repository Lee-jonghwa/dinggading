package com.mickey.dinggading.domain.membermatching.controller;

import com.mickey.dinggading.api.AttemptApi;
import com.mickey.dinggading.domain.membermatching.service.AttemptService;
import com.mickey.dinggading.model.AttemptDTO;
import com.mickey.dinggading.util.SecurityUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 시도 기록 관련 API를 처리하는 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attempts")
public class AttemptController implements AttemptApi {

    private final AttemptService attemptService;
    private final SecurityUtil securityUtil;

    /**
     * 특정 시도 기록의 상세 정보를 조회합니다.
     *
     * @param attemptId 시도 ID
     * @return 시도 상세 정보
     */
    @GetMapping("/{attempt_id}")
    public ResponseEntity<?> getAttempt(@PathVariable("attempt_id") Long attemptId) {
        AttemptDTO attempt = attemptService.getAttempt(attemptId);
        return ResponseEntity.ok(attempt);
    }

    /**
     * 연습 모드 시도 기록을 조회합니다.
     *
     * @param instrument 악기 (선택적)
     * @param pageable   페이징 정보
     * @return 연습 모드 시도 기록 페이지
     */
    @GetMapping("/practice")
    public ResponseEntity<Page<?>> getPracticeAttempts(
            @RequestParam(required = false) String instrument,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        UUID currentUser = securityUtil.getCurrentMemberId();
        Page<AttemptDTO> attempts = attemptService.getPracticeAttempts(currentUser, instrument,
                pageable);
        return ResponseEntity.ok(attempts);
    }

    /**
     * 랭크 모드 시도 기록을 조회합니다.
     *
     * @param instrument 악기 (선택적)
     * @param rankType   랭크 유형 (선택적)
     * @param pageable   페이징 정보
     * @return 랭크 모드 시도 기록 페이지
     */
    @GetMapping("/rank")
    public ResponseEntity<Page<?>> getRankAttempts(
            @RequestParam(required = false) String instrument,
            @RequestParam(required = false) String rankType,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        UUID currentUser = securityUtil.getCurrentMemberId();

        Page<AttemptDTO> attempts = attemptService.getRankAttempts(currentUser, instrument, rankType,
                pageable);
        return ResponseEntity.ok(attempts);
    }
}