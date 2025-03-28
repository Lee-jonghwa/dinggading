package com.mickey.dinggading.domain.membermatching.service;

import com.mickey.dinggading.base.status.ErrorStatus;
import com.mickey.dinggading.domain.membermatching.converter.RankMatchingConverter;
import com.mickey.dinggading.domain.membermatching.repository.AttemptRepository;
import com.mickey.dinggading.domain.membermatching.repository.RankMatchingRepository;
import com.mickey.dinggading.domain.memberrank.model.Attempt;
import com.mickey.dinggading.domain.memberrank.model.GameType;
import com.mickey.dinggading.domain.memberrank.model.Instrument;
import com.mickey.dinggading.domain.memberrank.model.RankMatching;
import com.mickey.dinggading.domain.memberrank.model.RankType;
import com.mickey.dinggading.domain.memberrank.model.Tier;
import com.mickey.dinggading.exception.ExceptionHandler;
import com.mickey.dinggading.model.AttemptDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 시도 기록 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final RankMatchingConverter rankMatchingConverter;
    private final RankMatchingService rankMatchingService;
    private final RankMatchingRepository rankMatchingRepository;

    /**
     * 특정 시도 기록의 상세 정보를 조회
     *
     * @param attemptId 시도 ID
     * @return 시도 DTO
     */
    public AttemptDTO getAttempt(Long attemptId) {
        Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.ATTEMPT_NOT_FOUND));
        return rankMatchingConverter.toDTO(attempt);
    }

    /**
     * 연습 모드 시도 기록을 페이지네이션하여 조회
     *
     * @param memberId      회원 ID
     * @param instrumentStr 악기 (선택)
     * @param pageable      페이징 정보
     * @return 시도 DTO 페이지
     */
    public Page<AttemptDTO> getPracticeAttempts(UUID memberId, String instrumentStr,
                                                Pageable pageable) {
        if (instrumentStr != null) {
            Instrument instrument = Instrument.valueOf(instrumentStr);
            return attemptRepository.findByMemberIdAndGameTypeAndInstrument(memberId, GameType.PRACTICE,
                            instrument, pageable)
                    .map(rankMatchingConverter::toDTO);
        } else {
            return attemptRepository.findByMemberIdAndGameType(memberId, GameType.PRACTICE, pageable)
                    .map(rankMatchingConverter::toDTO);
        }
    }

    /**
     * 랭크 모드 시도 기록을 페이지네이션하여 조회
     *
     * @param memberId      회원 ID
     * @param instrumentStr 악기 (선택)
     * @param rankTypeStr   랭크 유형 (선택)
     * @param pageable      페이징 정보
     * @return 시도 DTO 페이지
     */
    public Page<AttemptDTO> getRankAttempts(UUID memberId, String instrumentStr, String rankTypeStr,
                                            Pageable pageable) {
        if (instrumentStr != null && rankTypeStr != null) {
            Instrument instrument = Instrument.valueOf(instrumentStr);
            RankType rankType = RankType.valueOf(rankTypeStr);
            return attemptRepository.findByMemberIdAndGameTypeAndInstrumentAndRankType(
                            memberId, GameType.RANK, instrument, rankType, pageable)
                    .map(rankMatchingConverter::toDTO);
        } else if (instrumentStr != null) {
            Instrument instrument = Instrument.valueOf(instrumentStr);
            return attemptRepository.findByMemberIdAndGameTypeAndInstrument(
                            memberId, GameType.RANK, instrument, pageable)
                    .map(rankMatchingConverter::toDTO);
        } else {
            return attemptRepository.findByMemberIdAndGameType(memberId, GameType.RANK, pageable)
                    .map(rankMatchingConverter::toDTO);
        }
    }

    /**
     * 분석 결과를 기반으로 시도 기록 업데이트 (음성 분석 API에서 호출)
     *
     * @param attemptId 시도 ID
     * @param beatScore 박자 점수
     * @param tuneScore 음정 점수
     * @param toneScore 톤 점수
     */
    @Transactional
    public void updateAttemptScore(Long attemptId, Integer beatScore, Integer tuneScore,
                                   Integer toneScore) {
        Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.ATTEMPT_NOT_FOUND));

        // 목표 티어 정보 획득
        RankType rankType = attempt.getRankType();
        Tier targetTier = null;

        if (rankType != null) {
            // 랭크 매칭 정보에서 목표 티어 획득
            RankMatching rankMatching = rankMatchingRepository.findById(attempt.getRankMatching().getRankMatchingId())
                    .orElseThrow(() -> new ExceptionHandler(ErrorStatus.RANK_MATCHING_NOT_FOUND));
            targetTier = rankMatching.getTargetTier();
        }

        // 점수 설정 및 상태 업데이트
        attempt.setScores(beatScore, tuneScore, toneScore, targetTier);
        attemptRepository.save(attempt);

        // 랭크 매칭인 경우 매칭 상태 업데이트
        if (attempt.getGameType() == GameType.RANK) {
            rankMatchingService.processRankMatchingCompletion(attempt.getRankMatching());
        }
    }
}