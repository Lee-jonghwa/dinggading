package com.mickey.dinggading.domain.song.converter;

import com.mickey.dinggading.domain.memberrank.model.SongByInstrument;
import com.mickey.dinggading.model.SongByInstrumentDTO;
import com.mickey.dinggading.model.SongByInstrumentDTO.InstrumentEnum;
import com.mickey.dinggading.model.SongByInstrumentDTO.TierEnum;
import org.springframework.stereotype.Component;

@Component
public class SongByInstrumentConverter {
    private final SongBasicConverter songBasicConverter;

    public SongByInstrumentConverter(SongBasicConverter songBasicConverter) {
        this.songBasicConverter = songBasicConverter;
    }

    public SongByInstrumentDTO toDto(SongByInstrument entity) {
        if (entity == null) {
            return null;
        }

        SongByInstrumentDTO dto = SongByInstrumentDTO.builder()
                .songByInstrumentId(entity.getSongByInstrumentId())
                .songId(entity.getSong().getSongId())
                .songInstrumentPackId(entity.getSongInstrumentPack().getSongInstrumentPackId())
                .instrumentUrl(entity.getInstrumentUrl())
                .instrument(InstrumentEnum.valueOf(entity.getInstrument().name()))
                .tier(TierEnum.valueOf(entity.getTier().name()))
                .build();

        // Song 정보를 기본적인 형태로 포함
        if (entity.getSong() != null) {
            dto.setSong(songBasicConverter.toDto(entity.getSong()));
        }

        return dto;
    }
}