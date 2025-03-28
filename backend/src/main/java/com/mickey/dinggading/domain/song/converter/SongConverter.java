package com.mickey.dinggading.domain.song.converter;

import com.mickey.dinggading.domain.memberrank.model.Song;
import com.mickey.dinggading.model.CreateSongRequestDTO;
import com.mickey.dinggading.model.SongByInstrumentDTO;
import com.mickey.dinggading.model.SongDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SongConverter {
    private final SongByInstrumentConverter songByInstrumentConverter;

    public SongConverter(SongByInstrumentConverter songByInstrumentConverter) {
        this.songByInstrumentConverter = songByInstrumentConverter;
    }

    public SongDTO toDto(Song song) {
        if (song == null) {
            return null;
        }

        SongDTO dto = SongDTO.builder()
                .songId(song.getSongId())
                .title(song.getTitle())
                .description(song.getDescription())
                .youtubeUrl(song.getYoutubeUrl())
                .build();

        if (song.getSongByInstruments() != null && !song.getSongByInstruments().isEmpty()) {
            List<SongByInstrumentDTO> instrumentDtos = song.getSongByInstruments().stream()
                    .map(songByInstrumentConverter::toDto)
                    .collect(Collectors.toList());
            dto.setInstruments(instrumentDtos);
        }

        return dto;
    }

    public Song toEntity(CreateSongRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Song.createSong(
                dto.getTitle(),
                dto.getDescription(),
                dto.getYoutubeUrl()
        );
    }
}