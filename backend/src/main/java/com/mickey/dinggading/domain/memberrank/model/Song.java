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

    /**
     * 새로운 곡을 생성합니다.
     */
    public static Song createSong(String title, String description, String youtubeUrl) {
        return Song.builder()
                .title(title)
                .description(description)
                .youtubeUrl(youtubeUrl)
                .build();
    }

    /**
     * 곡 정보를 업데이트합니다.
     */
    public void updateSong(String title, String description, String youtubeUrl) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }

        this.description = description;

        if (youtubeUrl != null && !youtubeUrl.isBlank()) {
            this.youtubeUrl = youtubeUrl;
        }
    }

    /**
     * 악기별 곡 버전을 추가합니다.
     */
    public void addSongByInstrument(SongByInstrument songByInstrument) {
        this.songByInstruments.add(songByInstrument);
    }
}