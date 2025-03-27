package com.mickey.dinggading.domain.memberrank.model;

import com.mickey.dinggading.base.BaseEntity;
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
import java.time.LocalDate;
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
    private LocalDate defenceExpireDate;

    @Column(name = "last_attempt_date")
    private LocalDate lastAttemptDate;

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
                .lastAttemptTier(null) // 초기 null 상태
                .defenceExpireDate(null)
                .lastAttemptDate(null)
                .rankMatching(new ArrayList<>())
                .tierLogs(new ArrayList<>())
                .build();
    }

    /**
     * 티어를 업데이트합니다.
     */
    public void updateTier(Tier newTier) {
        Tier oldTier = this.tier;
        this.tier = newTier;

        MemberTierLog tierLog = MemberTierLog.createTierLog(this, oldTier, newTier);
        this.tierLogs.add(tierLog);
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
     * 도전 성공 횟수를 증가시킵니다.
     */
    public void incrementSuccessCount() {
        this.rankSuccessCount = this.rankSuccessCount + 1;
    }

    /**
     * 방어 만료일을 설정합니다.
     */
    public void setDefenceExpireDate(LocalDate expireDate) {
        this.defenceExpireDate = expireDate;
    }

    /**
     * 마지막 도전 정보를 업데이트합니다.
     */
    public void updateLastAttempt(Tier attemptTier, LocalDate attemptDate) {
        this.lastAttemptTier = attemptTier;
        this.lastAttemptDate = attemptDate;
    }
}