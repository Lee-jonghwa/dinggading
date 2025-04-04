package com.mickey.dinggading.domain.song.controller;

import com.mickey.dinggading.api.SongApi;
import com.mickey.dinggading.domain.song.service.SongService;
import com.mickey.dinggading.model.CreateSongByInstrumentRequestDTO;
import com.mickey.dinggading.model.CreateSongRequestDTO;
import com.mickey.dinggading.model.SongByInstrumentDTO;
import com.mickey.dinggading.model.SongDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SongController implements SongApi {
    private final SongService songService;

    /**
     * POST /songs/{songId}/instruments : 특정 곡에 악기별 연주 정보 추가 기존 곡에 새로운 악기별 연주 정보를 추가합니다.
     *
     * @param songId               곡 ID (required)
     * @param songByInstrumentInfo (required)
     * @param audioFile            녹음 파일 (mp3, wav 형식) (required)
     * @return SongByInstrumentDTO 악기별 곡 상세 정보 응답 (status code 201) or 잘못된 요청입니다. (status code 400) or 인증되지 않은 요청입니다.
     * (status code 401) or 권한이 없는 요청입니다. (status code 403) or 요청한 리소스를 찾을 수 없습니다. (status code 404)
     */
    @Override
    public ResponseEntity<?> addSongInstrument(Long songId, CreateSongByInstrumentRequestDTO songByInstrumentInfo,
                                               MultipartFile audioFile) {
        log.info("곡에 악기 추가 요청. 곡 ID: {}, 악기: {}", songId, songByInstrumentInfo.getInstrument());

        SongByInstrumentDTO result = songService.addSongInstrument(songId, songByInstrumentInfo);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    /**
     * POST /api/songs : 새로운 곡 등록 새로운 곡을 등록합니다.
     *
     * @param createSongRequestDTO 생성할 곡 정보 (required)
     * @return SongDTO 곡 응답 (status code 201)
     * @deprecated
     */
    @Override
    public ResponseEntity<?> createSong(CreateSongRequestDTO createSongRequestDTO) {
        log.info("새로운 곡 등록 요청. 제목: {}", createSongRequestDTO.getTitle());

        SongDTO result = songService.createSong(createSongRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    /**
     * GET /api/songs/{song_id} : 곡 상세 정보 조회 특정 곡의 상세 정보를 조회합니다.
     *
     * @param songId 곡 ID (required)
     * @return 곡 상세 정보 응답 (status code 200)
     */
    @Override
    public ResponseEntity<?> getSong(Long songId) {
        log.info("곡 상세 정보 조회 요청. 곡 ID: {}", songId);

        SongDTO result = songService.getSong(songId);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /songs/{songId}/instruments : 특정 곡의 악기별 연주 정보 조회 특정 곡의 악기별 연주 정보를 조회합니다.
     *
     * @param songId 곡 ID (required)
     * @return 악기별 곡 목록 응답 (status code 200)
     */
    @Override
    public ResponseEntity<?> getSongInstruments(Long songId) {
        log.info("곡의 악기별 버전 목록 조회 요청. 곡 ID: {}", songId);

        List<SongByInstrumentDTO> results = songService.getSongInstruments(songId);

        return ResponseEntity.ok(results);
    }

    /**
     * GET /api/songs : 곡 목록 조회 전체 곡 목록을 조회합니다. size, page를 쿼리 파라미터로 전달하여 페이징 처리가 가능합니다.
     *
     * @param pageable 페이징 정보
     * @return 곡 목록 페이지 응답 (status code 200)
     */
    @Override
    public ResponseEntity<?> getSongs(Pageable pageable) {
        log.info("전체 곡 목록 조회 요청. 페이지: {}, 사이즈: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<SongDTO> results = songService.getSongs(pageable);

        return ResponseEntity.ok(results);
    }

    /**
     * GET /api/songs/by-instrument/{instrument} : 악기별 곡 목록 조회 특정 악기에 해당하는 곡 목록을 조회합니다. size, page를 쿼리 파라미터로 전달하여 페이징
     * 처리가 가능합니다.
     *
     * @param instrument 악기 종류 (VOCAL, GUITAR, DRUM, BASS) (required)
     * @param pageable   페이징 정보
     * @return 곡 목록 페이지 응답 (status code 200)
     */
    @Override
    public ResponseEntity<?> getSongsByInstrument(String instrument, Pageable pageable) {
        log.info("악기별 곡 목록 조회 요청. 악기: {}, 페이지: {}, 사이즈: {}",
                instrument, pageable.getPageNumber(), pageable.getPageSize());

        Page<SongDTO> results = songService.getSongsByInstrument(instrument, pageable);

        return ResponseEntity.ok(results);
    }

    /**
     * GET /api/songs/by-tier/{tier} : 티어별 곡 목록 조회 특정 티어에 해당하는 곡 목록을 조회합니다. size, page를 쿼리 파라미터로 전달하여 페이징 처리가 가능합니다.
     *
     * @param tier     티어 등급 (UNRANKED, IRON, BRONZE, SILVER, GOLD, PLATINUM, DIAMOND) (required)
     * @param pageable 페이징 정보
     * @return 곡 목록 페이지 응답 (status code 200)
     */
    @Override
    public ResponseEntity<?> getSongsByTier(String tier, Pageable pageable) {
        log.info("티어별 곡 목록 조회 요청. 티어: {}, 페이지: {}, 사이즈: {}",
                tier, pageable.getPageNumber(), pageable.getPageSize());

        Page<SongDTO> results = songService.getSongsByTier(tier, pageable);

        return ResponseEntity.ok(results);
    }
}