package com.mickey.dinggading.domain.memberrank.model;

import com.mickey.dinggading.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Song")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Song extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id")
    private Long songId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "youtube_url", nullable = false, length = 255)
    private String youtubeUrl;

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SongByInstrument> songByInstruments = new ArrayList<>();

    // 생성자
    private Song(String title, String description, String youtubeUrl) {
        this.title = title;
        this.description = description;
        this.youtubeUrl = youtubeUrl;
    }

    // 팩토리 메소드
    public static Song createSong(String title, String description, String youtubeUrl) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("곡 제목은 필수입니다.");
        }
        if (youtubeUrl == null || youtubeUrl.isEmpty()) {
            throw new IllegalArgumentException("유튜브 URL은 필수입니다.");
        }

        return new Song(title, description, youtubeUrl);
    }

    // 악기별 곡 버전 추가 메소드
    public void addInstrumentVersion(Instrument instrument, Tier tier,
                                     String instrumentUrl, SongInstrumentPack pack) {
        // 이미 동일한 악기/티어 조합이 존재하는지 확인
        boolean alreadyExists = songByInstruments.stream()
                .anyMatch(sbi -> sbi.getInstrument() == instrument && sbi.getTier() == tier);

        if (alreadyExists) {
            throw new IllegalStateException("이미 동일한 악기와 티어 조합의 버전이 존재합니다.");
        }

        // pack의 티어와 악기가 파라미터와 일치하는지 확인
        if (pack.getSongPackTier() != tier || pack.getSongPackInstrument() != instrument) {
            throw new IllegalArgumentException("선택한 팩의 티어와 악기가 파라미터와 일치하지 않습니다.");
        }

        // 새 SongByInstrument 생성 및 추가
        SongByInstrument songByInstrument = SongByInstrument.createSongByInstrument(this, instrument, tier,
                instrumentUrl, pack);

        // 양방향 관계 설정
        songByInstruments.add(songByInstrument);

        // 팩에도 추가 (pack.addSong 내부에서 양방향 관계 설정)
        pack.addSong(songByInstrument);
    }

    // 곡 정보 업데이트
    public void updateSongInfo(String title, String description, String youtubeUrl) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("곡 제목은 필수입니다.");
        }
        if (youtubeUrl == null || youtubeUrl.isEmpty()) {
            throw new IllegalArgumentException("유튜브 URL은 필수입니다.");
        }

        this.title = title;
        this.description = description;
        this.youtubeUrl = youtubeUrl;
    }

    // 특정 악기의 모든 버전 조회
    public List<SongByInstrument> getVersionsByInstrument(Instrument instrument) {
        return songByInstruments.stream()
                .filter(sbi -> sbi.getInstrument() == instrument)
                .collect(Collectors.toList());
    }

    // 특정 티어의 모든 버전 조회
    public List<SongByInstrument> getVersionsByTier(Tier tier) {
        return songByInstruments.stream()
                .filter(sbi -> sbi.getTier() == tier)
                .collect(Collectors.toList());
    }

    // 특정 악기 버전 제거
    public void removeInstrumentVersion(SongByInstrument songByInstrument) {
        songByInstruments.remove(songByInstrument);
    }
}