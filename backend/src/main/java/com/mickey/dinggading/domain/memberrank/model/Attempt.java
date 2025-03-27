package com.mickey.dinggading.domain.memberrank.model;

import com.mickey.dinggading.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Attempt")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Attempt extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private Long attemptId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rank_matching_id", nullable = false)
    private RankMatching rankMatching;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_by_instrument_id", nullable = false)
    private SongByInstrument songByInstrument;

    @Column(name = "tune_score")
    private Integer tuneScore;

    @Column(name = "tone_score")
    private Integer toneScore;

    @Column(name = "beat_score")
    private Integer beatScore;

    @Column(name = "total_score", nullable = false)
    private Integer totalScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttemptStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_type", nullable = false)
    private GameType gameType;

    @Enumerated(EnumType.STRING)
    @Column(name = "rank_type")
    private RankType rankType;

    /**
     * 새로운 랭크 모드 시도를 생성합니다.
     */
    public static Attempt createRankAttempt(RankMatching rankMatching,
                                            SongByInstrument songByInstrument,
                                            Integer tuneScore,
                                            Integer toneScore,
                                            Integer beatScore) {
        // 총점 계산 (평균)
        int totalScore = calculateTotalScore(tuneScore, toneScore, beatScore);

        // 성공 여부 결정 (예: 60점 이상이면 성공)
        AttemptStatus status = totalScore >= 60 ? AttemptStatus.SUCCESS : AttemptStatus.FAIL;

        return Attempt.builder()
                .rankMatching(rankMatching)
                .songByInstrument(songByInstrument)
                .tuneScore(tuneScore)
                .toneScore(toneScore)
                .beatScore(beatScore)
                .totalScore(totalScore)
                .status(status)
                .gameType(GameType.RANK)
                .rankType(rankMatching.getRankType())
                .build();
    }

    /**
     * 새로운 연습 모드 시도를 생성합니다.
     */
    public static Attempt createPracticeAttempt(SongByInstrument songByInstrument,
                                                Integer tuneScore,
                                                Integer toneScore,
                                                Integer beatScore) {
        // 총점 계산 (평균)
        int totalScore = calculateTotalScore(tuneScore, toneScore, beatScore);

        // 연습 모드는 점수에 관계없이 성공/실패 여부 결정
        AttemptStatus status = totalScore >= 60 ? AttemptStatus.SUCCESS : AttemptStatus.FAIL;

        return Attempt.builder()
                .songByInstrument(songByInstrument)
                .tuneScore(tuneScore)
                .toneScore(toneScore)
                .beatScore(beatScore)
                .totalScore(totalScore)
                .status(status)
                .gameType(GameType.PRACTICE)
                .build();
    }

    /**
     * 총점을 계산합니다. (세 가지 점수의 평균)
     */
    private static int calculateTotalScore(Integer tuneScore, Integer toneScore, Integer beatScore) {
        int validScoreCount = 0;
        int totalScore = 0;

        if (tuneScore != null) {
            totalScore += tuneScore;
            validScoreCount++;
        }

        if (toneScore != null) {
            totalScore += toneScore;
            validScoreCount++;
        }

        if (beatScore != null) {
            totalScore += beatScore;
            validScoreCount++;
        }

        return validScoreCount > 0 ? totalScore / validScoreCount : 0;
    }

    // 점수 설정 메서드
    public void setScores(Integer beatScore, Integer tuneScore, Integer toneScore, Tier targetTier) {
        this.beatScore = beatScore;
        this.tuneScore = tuneScore;
        this.toneScore = toneScore;

        // 총점 계산 (평균)
        this.totalScore = (beatScore + tuneScore + toneScore) / 3;

        // 성공 여부 판단
        this.status = isScoreSufficient() ? AttemptStatus.SUCCESS : AttemptStatus.FAIL;
    }

    // 점수가 목표 티어에 충분한지 확인
    private boolean isScoreSufficient() {
        // 모든 티어에 대해 60점 이상을 합격으로 통일
        return totalScore >= 60;
    }
}