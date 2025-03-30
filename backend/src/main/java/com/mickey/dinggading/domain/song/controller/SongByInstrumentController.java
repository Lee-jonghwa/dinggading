package com.mickey.dinggading.domain.song.controller;

import com.mickey.dinggading.api.SongByInstrumentApi;
import com.mickey.dinggading.domain.song.service.SongByInstrumentService;
import com.mickey.dinggading.model.SongByInstrumentDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 악기별 곡 버전 관련 API 요청을 처리하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class SongByInstrumentController implements SongByInstrumentApi {

    private final SongByInstrumentService songByInstrumentService;

    /**
     * 특정 악기별 곡 버전 상세 정보 조회
     *
     * @param songByInstrumentId 악기별 곡 ID
     * @return 악기별 곡 상세 정보 응답
     */
    @Override
    public ResponseEntity<?> getSongByInstrument(@PathVariable("song_by_instrument_id") Long songByInstrumentId) {
        try {
            SongByInstrumentDTO dto = songByInstrumentService.getSongByInstrument(songByInstrumentId);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 특정 곡의 모든 악기별 버전 목록 조회
     *
     * @param songId 곡 ID
     * @return 곡의 악기별 버전 목록 응답
     */
    @Override
    public ResponseEntity<?> getSongInstruments1(@PathVariable("song_id") Long songId) {
        try {
            List<SongByInstrumentDTO> dtos = songByInstrumentService.getSongInstruments(songId);
            return ResponseEntity.ok(dtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}