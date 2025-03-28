package com.mickey.dinggading.domain.memberrank.model;

import com.mickey.dinggading.base.BaseEntity;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RankMatching")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class RankMatching extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rank_matching_id")
    private Long rankMatchingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_rank", nullable = false)
    private MemberRank memberRank;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MatchingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_instrument_pack_id", nullable = false)
    private SongInstrumentPack songInstrumentPack;

    @Column(name = "expire_date", nullable = false)
    private LocalDate expireDate;

    @Column(name = "started_at", nullable = false)
    private LocalDate startedAt;

    @Column(name = "attempt_count", nullable = false)
    private Integer attemptCount;

    @Column(name = "success_count", nullable = false)
    private Integer successCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "instrument", nullable = false)
    private Instrument instrument;

    @Enumerated(EnumType.STRING)
    @Column(name = "rank_type", nullable = false)
    private RankType rankType;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_tier", nullable = false)
    private Tier targetTier;

    @OneToMany(mappedBy = "rankMatching", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attempt> attempts = new ArrayList<>();

    /**
     * 새로운 랭크 매칭을 생성합니다.
     */
    public static RankMatching createRankMatching(SongInstrumentPack songInstrumentPack,
                                                  Instrument instrument,
                                                  RankType rankType,
                                                  Tier targetTier) {
        LocalDate now = LocalDate.now();
        LocalDate expireDate = now.plusDays(7);

        return RankMatching.builder()
                .status(MatchingStatus.IN_PROGRESS)
                .songInstrumentPack(songInstrumentPack)
                .expireDate(expireDate)
                .startedAt(now)
                .attemptCount(0)
                .successCount(0)
                .instrument(instrument)
                .rankType(rankType)
                .targetTier(targetTier)
                .build();
    }

    /**
     * 시도를 진행하고 결과를 업데이트합니다.
     */
    public void addAttempt(Attempt attempt) {
        this.attempts.add(attempt);
        this.attemptCount++;

        if (attempt.getStatus() == AttemptStatus.SUCCESS) {
            this.successCount++;

            // 성공 횟수가 3 이상이면 완료 상태로 변경
            if (this.successCount >= 3) {
                this.status = MatchingStatus.COMPLETED;
            }
        }

        // 시도 횟수가 5회 이상이고 성공 횟수가 3회 미만이면 실패 상태로 변경
        if (this.attemptCount >= 5 && this.successCount < 3) {
            this.status = MatchingStatus.FAILED;
        }
    }

    /* ---------------------- 티어 변동 ----------------------*/

    public boolean isFirstRankType() {
        return this.rankType == RankType.FIRST;
    }

    public void updateRankMatching(MemberRank memberRank) {
        if (this.memberRank != null) {
            this.memberRank.getRankMatching().remove(this);
        }
        this.memberRank = memberRank;
        memberRank.getRankMatching().add(this);

    }



    /* ---------------------- 도전 날자 ----------------------*/

    /**
     * 만료 처리를 합니다.
     */
    public void expire() {
        if (this.status == MatchingStatus.IN_PROGRESS) {
            this.status = MatchingStatus.EXPIRED;
        }
    }

    public boolean isCompleted() {
        return this.status == MatchingStatus.COMPLETED;
    }

    /**
     * 만료 여부를 확인합니다.
     */
    public boolean isExpired() {
        return this.status == MatchingStatus.EXPIRED;
    }

    /**
     * 매칭이 완료 조건을 충족하는지 확인 최대 시도 횟수(5회)에 도달했거나 성공 횟수(3회)를 달성한 경우 완료 처리 가능
     *
     * @return 완료 조건 충족 여부
     */
    public boolean isEligibleForCompletion() {
        return getAttemptCount() >= 5 || getSuccessCount() >= 3;
    }

    /**
     * 매칭이 성공적으로 완료되었는지 확인 3회 이상 성공했을 경우 매칭 성공으로 간주
     *
     * @return 매칭 성공 여부
     */
    public boolean isSuccessful() {
        return getSuccessCount() >= 3;
    }

    /**
     * 매칭 상태를 완료 처리 성공 여부에 따라 COMPLETED 또는 FAILED 상태로 설정
     */
    public void complete() {
        if (isSuccessful()) {
            this.status = MatchingStatus.COMPLETED;
        } else {
            this.status = MatchingStatus.FAILED;
        }
    }
}