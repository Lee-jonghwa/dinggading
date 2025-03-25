package com.mickey.dinggading.domain.band.model.entity;

import com.mickey.dinggading.base.BaseEntity;
import com.mickey.dinggading.domain.member.model.entity.Member;
import com.mickey.dinggading.model.Instrument;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BandMember")
public class BandMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "band_member_id")
    private Long bandMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id", nullable = false)
    private Band band;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "instrument")
    private Instrument instrument;

    // 악기 업데이트 메소드
    public void updateInstrument(Instrument instrument) {
        this.instrument = instrument;
    }
}