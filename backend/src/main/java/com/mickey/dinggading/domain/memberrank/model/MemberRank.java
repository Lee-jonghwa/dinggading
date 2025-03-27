package com.mickey.dinggading.domain.memberrank.model;

import com.mickey.dinggading.base.BaseEntity;
import com.mickey.dinggading.domain.TimeWrapper;
import com.mickey.dinggading.domain.member.model.entity.Member;
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
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MemberRank")
@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRank extends BaseEntity {

    private static final int DEFENCE_PERIOD_DAYS = 30;
    private static final int TIER_MAINTENANCE_PERIOD_DAYS = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_rank_id")
    private Long memberRankId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "instrument", nullable = false)
    private Instrument instrument;

    @Enumerated(EnumType.STRING)
    @Column(name = "tier", nullable = false)
    private Tier tier;

    @Column(name = "beat_score")
    private Integer beatScore;

    @Column(name = "tune_score")
    private Integer tuneScore;

    @Column(name = "tone_score")
    private Integer toneScore;

    @Column(name = "rank_success_count", columnDefinition = "int default 0")
    private Integer rankSuccessCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_attempt_tier")
    private Tier lastAttemptTier;

    @Column(name = "defence_expire_date")
    private LocalDateTime defenceExpireDate;

    @Column(name = "last_attempt_date")
    private LocalDateTime lastAttemptDate;

    @Transient
    private LocalDateTime tierQualificationExpireDate; // 추가: 티어 유지 자격 만료일

    @OneToMany(mappedBy = "memberRank", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RankMatching> rankMatching = new ArrayList<>();

    @OneToMany(mappedBy = "memberRank", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTierLog> tierLogs = new ArrayList<>();

    /**
     * 새로운 멤버 랭크를 생성합니다.
     */
    public static MemberRank createMemberRank(Instrument instrument) {
        return MemberRank.builder()
                .member(null) // 초기 멤버는 null, updateMember로 업데이트 필요
                .instrument(instrument)
                .tier(Tier.UNRANKED)
                .beatScore(0)
                .tuneScore(0)
                .toneScore(0)
                .rankSuccessCount(0)
                .lastAttemptTier(Tier.UNRANKED) // 초기 null 상태
                .defenceExpireDate(null)
                .lastAttemptDate(null)
                .rankMatching(new ArrayList<>())
                .tierLogs(new ArrayList<>())
                .build();
    }

    /**
     * 티어 도전이 가능한지 여부 확인
     *
     * @return 도전 가능 여부
     */
    public boolean canChallenge() {
        // UNRANKED 상태인 경우 도전 불가능(배치고사 필요)
        if (Tier.UNRANKED.equals(this.tier)) {
            return false;
        }

        // DIAMOND는 최고 티어라 도전 불가능
        return !Tier.DIAMOND.equals(this.tier);
    }

    /**
     * 배치고사할 수 있는지 여부
     *
     * @return
     */
    public boolean canFirst() {
        return this.tier.equals(Tier.UNRANKED);
    }

    /**
     * 현재 티어 방어가 필요한지 여부 확인 티어 유지 자격이 만료되었고 현재 UNRANKED가 아닌 경우 방어가 필요함
     *
     * @param time 현재 시간 래퍼(테스트 용이성을 위한 의존성 주입)
     * @return 방어 필요 여부
     */
    public boolean needsDefence(TimeWrapper time) {
        // UNRANKED 상태는 방어가 필요 없음
        if (tier == Tier.UNRANKED) {
            return false;
        }

        // 현재 날짜가 티어 유지 자격 만료일 이후인 경우 방어 필요
        LocalDateTime currentDate = time.now();
        return currentDate.isAfter(tierQualificationExpireDate);
    }

    /*
     * 멤버를 업데이트 합니다.
     */
    public void updateMember(Member member) {
        if (this.member != null) {
            this.member.getMemberRanks().remove(this);
        }

        this.member = member;
        member.getMemberRanks().add(this);
    }

    /**
     * 점수를 업데이트합니다.
     */
    public void updateScores(Integer beatScore, Integer tuneScore, Integer toneScore) {
        this.beatScore = beatScore;
        this.tuneScore = tuneScore;
        this.toneScore = toneScore;
    }

    /**
     * 티어를 업데이트합니다.
     */
    private void updateTier(LocalDateTime today, Tier newTier) {
        Tier oldTier = this.tier;
        this.tier = newTier;

        MemberTierLog tierLog = MemberTierLog.createTierLog(this, oldTier, newTier);
        this.tierLogs.add(tierLog);

    }

    /**
     * 티어 승급 처리 (도전 성공 시)
     *
     * @return 생성된 티어 로그
     */
    public void promote(LocalDateTime today) {
        Tier promoteTier = Tier.values()[this.tier.ordinal() - 1];
        if (this.tier != Tier.IRON) {
            updateTier(today, promoteTier);
            renewTierQualification(today, promoteTier);
        }
    }

    /**
     * 티어 강등 처리 방어 실패 시 현재 티어에서 한 단계 낮은 티어로 강등 단, IRON 티어는 더 이상 강등되지 않음
     */
    public void demote(LocalDateTime today) {
        Tier demoteTier = Tier.values()[this.tier.ordinal() - 1];
        if (this.tier != Tier.IRON) {
            updateTier(today, demoteTier);
            renewTierQualification(today, demoteTier);
        }
    }

    /**
     * 배치고사로 성공했을 때 티어 승급시키기
     *
     * @param today
     * @param targetTier
     */
    public void firstMatchingPromote(LocalDateTime today, Tier targetTier) {
        updateTier(today, targetTier);
        renewTierQualification(today, targetTier);
        incrementSuccessCount();
    }

    /**
     * 랭크 매칭 성공 시 티어 유지 자격 갱신
     *
     * @return 갱신된 유지 자격 만료일
     */
    private LocalDateTime renewTierQualification(LocalDateTime today, Tier attemptedTier) {
        // 랭크 매칭 성공 후 30일 동안의 티어 유지 자격 부여
        this.lastAttemptDate = today;
        // 티어 유지 자격 만료일 갱신
        this.lastAttemptTier = attemptedTier;

        this.tierQualificationExpireDate = today.plusDays(TIER_MAINTENANCE_PERIOD_DAYS);

        return this.lastAttemptDate;
    }

    public boolean isInDefencePeriod(LocalDateTime currentTime) {

        return !currentTime.isBefore(defenceExpireDate) &&
                !currentTime.isAfter(
                        defenceExpireDate.plusDays(DEFENCE_PERIOD_DAYS)
                );
    }

    /**
     * 랭크 성공 횟수 증가 도전 성공 시 성공 횟수를 1 증가시킴
     */
    private void incrementSuccessCount() {
        this.rankSuccessCount += 1;
    }

    /**
     * 배치고사 실패 기록 배치고사 실패 시 마지막으로 도전한 티어 정보 기록
     *
     * @param attemptedTier 도전했던 티어
     */
    public void recordFailedPlacement(Tier attemptedTier) {
        this.lastAttemptTier = attemptedTier;
    }

}