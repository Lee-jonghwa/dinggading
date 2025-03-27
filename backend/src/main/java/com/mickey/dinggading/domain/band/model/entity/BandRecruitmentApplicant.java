package com.mickey.dinggading.domain.band.model.entity;

import com.mickey.dinggading.base.BaseEntity;
import com.mickey.dinggading.domain.member.model.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "BandRecruitmentApplicant")
public class BandRecruitmentApplicant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "band_recruitment_applicant_id")
    private Long bandRecruitmentApplicantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_recruitment_instruments_id", nullable = false)
    private BandRecruitmentInstrument bandRecruitmentInstrument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private Member applicant;

    @Column(name = "apply_date", nullable = false)
    private LocalDateTime applyDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicantStatus status;

    /**
     * 밴드 구인 악기 포지션 설정
     */
    public void setBandRecruitmentInstrument(BandRecruitmentInstrument bandRecruitmentInstrument) {
        this.bandRecruitmentInstrument = bandRecruitmentInstrument;
    }

    /**
     * 지원 상태 업데이트
     */
    public void updateStatus(ApplicantStatus status) {
        this.status = status;
    }

    /**
     * 지원 승인
     */
    public void accept() {
        this.status = ApplicantStatus.ACCEPTED;
    }

    /**
     * 지원 거절
     */
    public void reject() {
        this.status = ApplicantStatus.REJECTED;
    }

    /**
     * 지원자 상태 열거형
     */
    public enum ApplicantStatus {
        PENDING,   // 대기 중
        ACCEPTED,  // 승인됨
        REJECTED   // 거절됨
    }
    
    /**
     * 지원 날짜 자동 설정을 위한 PrePersist
     */
    @PrePersist
    public void prePersist() {
        if (this.applyDate == null) {
            this.applyDate = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = ApplicantStatus.PENDING;
        }
    }
}