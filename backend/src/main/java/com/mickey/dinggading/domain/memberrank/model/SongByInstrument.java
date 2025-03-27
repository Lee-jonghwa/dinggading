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
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SongByInstrument")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class SongByInstrument extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_by_instrument_id")
    private Long songByInstrumentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_instrument_pack_id", nullable = false)
    private SongInstrumentPack songInstrumentPack;

    @Column(name = "instrument_url", nullable = false, length = 255)
    private String instrumentUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "instrument", nullable = false)
    private Instrument instrument;

    @Enumerated(EnumType.STRING)
    @Column(name = "tier", nullable = false)
    private Tier tier;

    @OneToMany(mappedBy = "songByInstrument", cascade = CascadeType.ALL)
    private List<Attempt> attempts = new ArrayList<>();

    /**
     * 새로운 악기별 곡 버전을 생성합니다.
     */
    public static SongByInstrument createSongByInstrument(
            Song song,
            SongInstrumentPack songInstrumentPack,
            String instrumentUrl,
            Instrument instrument,
            Tier tier
    ) {
        return SongByInstrument.builder()
                .song(song)
                .songInstrumentPack(songInstrumentPack)
                .instrumentUrl(instrumentUrl)
                .instrument(instrument)
                .tier(tier)
                .build();
    }

    /**
     * 악기별 곡 정보를 업데이트합니다.
     */
    public void updateInstrumentUrl(String instrumentUrl) {
        if (instrumentUrl != null && !instrumentUrl.isBlank()) {
            this.instrumentUrl = instrumentUrl;
        }
    }

    /**
     * 티어를 업데이트합니다.
     */
    public void updateTier(Tier tier) {
        this.tier = tier;
    }
}