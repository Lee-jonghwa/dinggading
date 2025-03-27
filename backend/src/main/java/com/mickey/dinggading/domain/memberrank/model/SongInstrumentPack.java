package com.mickey.dinggading.domain.memberrank.model;

import com.mickey.dinggading.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "SongInstrumentPack")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class SongInstrumentPack extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_instrument_pack_id")
    private Long songInstrumentPackId;

    @Column(name = "pack_name", nullable = false, length = 255)
    private String packName;

    @Enumerated(EnumType.STRING)
    @Column(name = "song_pack_tier", nullable = false)
    private Tier songPackTier;

    @Enumerated(EnumType.STRING)
    @Column(name = "song_pack_instrument", nullable = false)
    private Instrument songPackInstrument;

    @OneToMany(mappedBy = "songInstrumentPack", cascade = CascadeType.ALL)
    private List<SongByInstrument> songByInstruments = new ArrayList<>();

    @OneToMany(mappedBy = "songInstrumentPack", cascade = CascadeType.ALL)
    private List<RankMatching> rankMatchings = new ArrayList<>();

    /**
     * 새로운 곡 팩을 생성합니다.
     */
    public static SongInstrumentPack createSongInstrumentPack(String packName, Tier tier, Instrument instrument) {
        return SongInstrumentPack.builder()
                .packName(packName)
                .songPackTier(tier)
                .songPackInstrument(instrument)
                .build();
    }

    /**
     * 팩에 악기별 곡을 추가합니다.
     */
    public void addSongByInstrument(SongByInstrument songByInstrument) {
        this.songByInstruments.add(songByInstrument);
    }

    /**
     * 팩 정보를 업데이트합니다.
     */
    public void updatePackInfo(String packName) {
        if (packName != null && !packName.isBlank()) {
            this.packName = packName;
        }
    }
}