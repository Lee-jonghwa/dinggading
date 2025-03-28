package com.mickey.dinggading.domain.band.model.entity;

import com.mickey.dinggading.base.BaseEntity;
import com.mickey.dinggading.model.AddRecruitmentInstrumentRequest;
import com.mickey.dinggading.model.Instrument;
import com.mickey.dinggading.model.Tier;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "BandRecruitmentInstrument")
public class BandRecruitmentInstrument extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "band_recruitment_instruments_id")
    private Long bandRecruitmentInstrumentsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_recruitment_id", nullable = false)
    private BandRecruitment bandRecruitment;

    @Enumerated(EnumType.STRING)
    @Column(name = "instrument", nullable = false)
    private Instrument instrument;

    @Enumerated(EnumType.STRING)
    @Column(name = "required_tier", nullable = false)
    private Tier requiredTier;

    @Column(name = "max_size", nullable = false)
    private Integer maxSize;

    @OneToMany(mappedBy = "bandRecruitmentInstrument", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BandRecruitmentApplicant> applicants = new ArrayList<>();

    public BandRecruitmentInstrument(Instrument instrument, Tier requiredTier, Integer maxSize) {
        this.instrument = instrument;
        this.requiredTier = requiredTier;
        this.maxSize = maxSize;
    }

    /**
     * BandRecruitment 설정
     */
    public void setBandRecruitment(BandRecruitment bandRecruitment) {
        this.bandRecruitment = bandRecruitment;
    }

    /**
     * 지원자 추가
     */
    public void addApplicant(BandRecruitmentApplicant applicant) {
        this.applicants.add(applicant);
        applicant.setBandRecruitmentInstrument(this);
    }

    /**
     * 지원자 제거
     */
    public void removeApplicant(BandRecruitmentApplicant applicant) {
        this.applicants.remove(applicant);
        applicant.setBandRecruitmentInstrument(null);
    }

    /**
     * 현재 지원자 수 조회
     */
    public int getCurrentApplicantsCount() {
        return this.applicants.size();
    }

    /**
     * 추가 지원 가능 여부 확인
     */
    public boolean canApply() {
        return getCurrentApplicantsCount() < maxSize;
    }
}