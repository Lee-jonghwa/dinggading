package com.mickey.dinggading.domain.band.model.entity;

import com.mickey.dinggading.base.BaseEntity;
import com.mickey.dinggading.domain.memberrank.model.Song;
import com.mickey.dinggading.model.RecruitmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "BandRecruitment")
public class BandRecruitment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "band_recruitment_id")
    private Long bandRecruitmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id", nullable = false)
    private Band band;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "audition_date", nullable = false)
    private LocalDateTime auditionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audition_song_id", nullable = false)
    private Song auditionSong;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RecruitmentStatus status;

    @OneToMany(mappedBy = "bandRecruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BandRecruitmentInstrument> instruments = new ArrayList<>();

    /**
     * 구인 공고 상태를 업데이트합니다.
     */
    public void updateStatus(RecruitmentStatus status) {
        this.status = status;
    }

    /**
     * 구인 공고 정보를 업데이트합니다.
     */
    public void update(String title, String description, LocalDateTime auditionDate, Song auditionSong) {
        this.title = title;
        this.description = description;
        this.auditionDate = auditionDate;
        this.auditionSong = auditionSong;
    }

    /**
     * 악기 포지션을 추가합니다.
     */
    public void addInstrument(BandRecruitmentInstrument instrument) {
        this.instruments.add(instrument);
        instrument.setBandRecruitment(this);
    }

    /**
     * 악기 포지션을 제거합니다.
     */
    public void removeInstrument(BandRecruitmentInstrument instrument) {
        this.instruments.remove(instrument);
        instrument.setBandRecruitment(null);
    }
}