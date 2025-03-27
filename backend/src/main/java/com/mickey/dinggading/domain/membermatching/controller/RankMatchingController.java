package com.mickey.dinggading.domain.membermatching.controller;

import com.mickey.dinggading.api.RankMatchingApi;
import com.mickey.dinggading.domain.membermatching.service.RankMatchingService;
import com.mickey.dinggading.domain.memberrank.model.Instrument;
import com.mickey.dinggading.model.CreateRankMatchingRequestDTO;
import com.mickey.dinggading.model.RankMatchingDTO;
import com.mickey.dinggading.util.SecurityUtil;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 랭크 매칭 관련 API를 처리하는 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class RankMatchingController implements RankMatchingApi {

    private final SecurityUtil securityUtil;
    private final RankMatchingService rankMatchingService;

    /**
     * POST /api/rank-matchings : 새로운 랭크 매칭 생성 새로운 랭크 매칭을 생성합니다. 티어 도전, 방어, 배치 모드를 지정하여 생성할 수 있습니다.
     *
     * @param createRankMatchingRequestDTO (required)
     * @return 랭크 매칭 응답 (status code 201) or 잘못된 요청입니다. (status code 400) or 인증되지 않은 요청입니다. (status code 401) or 지정한 곡
     * 팩을 찾을 수 없습니다. (status code 404)
     */
    @Override
    public ResponseEntity<RankMatchingDTO> createRankMatching(
            CreateRankMatchingRequestDTO createRankMatchingRequestDTO) {
        UUID currentUser = securityUtil.getCurrentMemberId();

        RankMatchingDTO createdMatching = rankMatchingService.createRankMatching(currentUser,
                createRankMatchingRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMatching);
    }

    /**
     * GET /api/rank-matchings/{instrument}/available : 도전 가능한 랭크 매칭 조회 현재 로그인한 회원이 도전 가능한 랭크 매칭 정보를 조회합니다.
     *
     * @param instrument 악기 종류 필터링 (VOCAL, GUITAR, DRUM, BASS) (required)
     * @return 도전 가능한 랭크 매칭 응답 (status code 200) or 인증되지 않은 요청입니다. (status code 401)
     */
    @Override
    public ResponseEntity<?> getAvailableRankMatchings(String instrument) {
        UUID currentUser = securityUtil.getCurrentMemberId();
        Instrument instrumentEnum = Instrument.valueOf(instrument.toUpperCase());
        Map<String, Object> availableMatchings = rankMatchingService.getAvailableRankMatchings(
                currentUser, instrumentEnum
        );
        return ResponseEntity.ok(availableMatchings);
    }

    @GetMapping("/api/rank-matchings/available/{instrument}")
    public ResponseEntity<?> getAvailableRankMatching(
            @PathVariable(name = "instrument") Instrument instrument
    ) {
        UUID currentUser = securityUtil.getCurrentMemberId();

        return ResponseEntity.ok(null);
    }

    /**
     * GET /api/rank-matchings/{rank_matching_id} : 랭크 매칭 상세 정보 조회 특정 랭크 매칭의 상세 정보를 조회합니다.
     *
     * @param rankMatchingId 랭크 매칭 ID (required)
     * @return 랭크 매칭 상세 정보 응답 (status code 200) or 요청한 리소스를 찾을 수 없습니다. (status code 404)
     */
    @Override
    public ResponseEntity<RankMatchingDTO> getRankMatching(
            @PathVariable("rank_matching_id") Long rankMatchingId) {
        RankMatchingDTO matching = rankMatchingService.getRankMatching(rankMatchingId);
        return ResponseEntity.ok(matching);
    }

    /**
     * GET /api/rank-matchings/history : 회원 랭크 매칭 이력 조회 현재 로그인한 회원의 랭크 매칭 이력을 조회합니다. size, page를 쿼리 파라미터로 전달하여 페이징 처리가
     * 가능합니다.
     *
     * @param pageable
     * @return 랭크 매칭 목록 페이지 응답 (status code 200) or 인증되지 않은 요청입니다. (status code 401)
     * @deprecated
     */
    @Override
    public ResponseEntity<Page<RankMatchingDTO>> getRankMatchingHistory(
            @PageableDefault(size = 10, sort = "startedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        UUID currentUser = securityUtil.getCurrentMemberId();

        Page<RankMatchingDTO> history = rankMatchingService.getRankMatchingHistory(currentUser, pageable);
        return ResponseEntity.ok(history);
    }

    /**
     * GET /api/rank-matchings : 랭크 매칭 목록 조회 전체 랭크 매칭 목록을 조회합니다. size, page를 쿼리 파라미터로 전달하여 페이징 처리가 가능합니다.
     *
     * @param pageable
     * @return 랭크 매칭 목록 페이지 응답 (status code 200)
     * @deprecated
     */
    @Override
    public ResponseEntity<Page<RankMatchingDTO>> getRankMatchings(
            @PageableDefault(size = 10, sort = "rankMatchingId", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<RankMatchingDTO> matchings = rankMatchingService.getRankMatchings(pageable);
        return ResponseEntity.ok(matchings);
    }
}