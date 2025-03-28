package com.mickey.dinggading.domain.song.service;

import com.mickey.dinggading.base.status.ErrorStatus;
import com.mickey.dinggading.domain.memberrank.model.Instrument;
import com.mickey.dinggading.domain.memberrank.model.Song;
import com.mickey.dinggading.domain.memberrank.model.SongByInstrument;
import com.mickey.dinggading.domain.memberrank.model.SongInstrumentPack;
import com.mickey.dinggading.domain.memberrank.model.Tier;
import com.mickey.dinggading.domain.song.converter.SongByInstrumentConverter;
import com.mickey.dinggading.domain.song.converter.SongConverter;
import com.mickey.dinggading.domain.song.repository.SongByInstrumentRepository;
import com.mickey.dinggading.domain.song.repository.SongInstrumentPackRepository;
import com.mickey.dinggading.domain.song.repository.SongRepository;
import com.mickey.dinggading.exception.ExceptionHandler;
import com.mickey.dinggading.model.AddSongInstrumentRequest;
import com.mickey.dinggading.model.CreateSongRequestDTO;
import com.mickey.dinggading.model.CreateSongRequestDTOInstrumentsInner;
import com.mickey.dinggading.model.SongByInstrumentDTO;
import com.mickey.dinggading.model.SongDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SongService {
    private final SongRepository songRepository;
    private final SongByInstrumentRepository songByInstrumentRepository;
    private final SongInstrumentPackRepository songInstrumentPackRepository;
    private final SongConverter songConverter;
    private final SongByInstrumentConverter songByInstrumentConverter;

    /**
     * 모든 곡 목록을 페이지네이션으로 조회합니다.
     */
    public Page<SongDTO> getSongs(Pageable pageable) {
        log.info("모든 곡 목록 조회. 페이지: {}, 사이즈: {}", pageable.getPageNumber(), pageable.getPageSize());
        return songRepository.findAll(pageable)
                .map(songConverter::toDto);
    }

    /**
     * 특정 곡의 상세 정보를 조회합니다.
     */
    public SongDTO getSong(Long songId) {
        log.info("곡 상세 정보 조회. 곡 ID: {}", songId);
        Song song = findSongById(songId);
        return songConverter.toDto(song);
    }

    /**
     * 악기별 곡 목록을 조회합니다.
     */
    public Page<SongDTO> getSongsByInstrument(String instrumentStr, Pageable pageable) {
        log.info("악기별 곡 목록 조회. 악기: {}, 페이지: {}, 사이즈: {}",
                instrumentStr, pageable.getPageNumber(), pageable.getPageSize());

        Instrument instrument = parseInstrument(instrumentStr);
        return songRepository.findAllByInstrument(instrument, pageable)
                .map(songConverter::toDto);
    }

    /**
     * 티어별 곡 목록을 조회합니다.
     */
    public Page<SongDTO> getSongsByTier(String tierStr, Pageable pageable) {
        log.info("티어별 곡 목록 조회. 티어: {}, 페이지: {}, 사이즈: {}",
                tierStr, pageable.getPageNumber(), pageable.getPageSize());

        Tier tier = parseTier(tierStr);
        return songRepository.findAllByTier(tier, pageable)
                .map(songConverter::toDto);
    }

    /**
     * 특정 곡의 악기별 버전 목록을 조회합니다.
     */
    public List<SongByInstrumentDTO> getSongInstruments(Long songId) {
        log.info("곡의 악기별 버전 목록 조회. 곡 ID: {}", songId);
        Song song = findSongById(songId);

        return song.getSongByInstruments().stream()
                .map(songByInstrumentConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 새로운 곡을 등록합니다.
     */
    @Transactional
    public SongDTO createSong(CreateSongRequestDTO requestDTO) {
        log.info("새로운 곡 등록 시작. 제목: {}", requestDTO.getTitle());

        // 도메인 모델의 createSong 메서드 활용하여 기본 곡 정보 생성
        Song song = Song.createSong(
                requestDTO.getTitle(),
                requestDTO.getDescription(),
                requestDTO.getYoutubeUrl()
        );

        // 기본 곡 정보 저장
        Song savedSong = songRepository.save(song);

        // 악기별 버전 처리 (존재하는 경우에만)
        if (hasNoInstruments(requestDTO)) {
            log.info("새로운 곡 등록 완료. 곡 ID: {}", savedSong.getSongId());
            return songConverter.toDto(savedSong);
        }

        // 악기별 버전 추가
        addInstrumentVersions(savedSong, requestDTO.getInstruments());

        // 변경된 엔티티 저장
        savedSong = songRepository.save(savedSong);

        log.info("새로운 곡 등록 완료. 곡 ID: {}", savedSong.getSongId());
        return songConverter.toDto(savedSong);
    }

    /**
     * 기존 곡에 특정 악기 버전을 추가합니다.
     */
    @Transactional
    public SongByInstrumentDTO addSongInstrument(Long songId, AddSongInstrumentRequest request) {
        log.info("곡에 악기 버전 추가. 곡 ID: {}, 악기: {}", songId, request.getInstrument());

        // 곡 존재 확인
        Song song = findSongById(songId);
        Instrument instrument = parseInstrument(request.getInstrument().getValue());

        // 이미 해당 악기 버전이 존재하는지 확인
        checkDuplicateInstrument(songId, instrument);

        // 적절한 팩 찾기
        SongInstrumentPack pack = findPackByInstrument(instrument);

        // 악기 버전 추가 및 저장
        addInstrumentToSong(song, instrument, request.getMediaUrl(), pack);

        // 추가된 악기 버전 찾기
        SongByInstrument addedInstrument = findAddedInstrument(song, instrument);

        log.info("곡에 악기 버전 추가 완료. 곡 ID: {}, 악기: {}", songId, instrument);
        return songByInstrumentConverter.toDto(addedInstrument);
    }

    // 헬퍼 메서드 - ID로 곡 찾기
    private Song findSongById(Long songId) {
        return songRepository.findById(songId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.SONG_NOT_FOUND));
    }

    // 헬퍼 메서드 - 악기 문자열 파싱
    private Instrument parseInstrument(String instrumentStr) {
        try {
            return Instrument.valueOf(instrumentStr);
        } catch (IllegalArgumentException e) {
            log.error("유효하지 않은 악기: {}", instrumentStr);
            throw new ExceptionHandler(ErrorStatus.INVALID_INSTRUMENT);
        }
    }

    // 헬퍼 메서드 - 티어 문자열 파싱
    private Tier parseTier(String tierStr) {
        try {
            return Tier.valueOf(tierStr);
        } catch (IllegalArgumentException e) {
            log.error("유효하지 않은 티어: {}", tierStr);
            throw new ExceptionHandler(ErrorStatus.INVALID_TIER);
        }
    }

    // 헬퍼 메서드 - 악기 버전 존재 여부 확인
    private void checkDuplicateInstrument(Long songId, Instrument instrument) {
        if (songByInstrumentRepository.existsBySong_SongIdAndInstrument(songId, instrument)) {
            log.error("이미 해당 악기 버전이 존재합니다. 곡 ID: {}, 악기: {}", songId, instrument);
            throw new ExceptionHandler(ErrorStatus.DUPLICATE_INSTRUMENT_VERSION);
        }
    }

    // 헬퍼 메서드 - 악기로 적절한 팩 찾기
    private SongInstrumentPack findPackByInstrument(Instrument instrument) {
        return songInstrumentPackRepository.findAll().stream()
                .filter(p -> p.getSongPackInstrument() == instrument)
                .findFirst()
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.SONG_INSTRUMENT_PACK_NOT_FOUND));
    }

    // 헬퍼 메서드 - 악기 버전 추가
    private void addInstrumentToSong(Song song, Instrument instrument, String mediaUrl, SongInstrumentPack pack) {
        try {
            song.addInstrumentVersion(
                    instrument,
                    pack.getSongPackTier(),
                    mediaUrl,
                    pack
            );
            songRepository.save(song);
        } catch (IllegalStateException e) {
            log.error("도메인 모델 제약 조건 위반: {}", e.getMessage());
            throw new ExceptionHandler(ErrorStatus.DUPLICATE_INSTRUMENT_TIER_COMBINATION);
        }
    }

    // 헬퍼 메서드 - 추가된 악기 버전 찾기
    private SongByInstrument findAddedInstrument(Song song, Instrument instrument) {
        return song.getSongByInstruments().stream()
                .filter(sbi -> sbi.getInstrument() == instrument)
                .findFirst()
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.SONG_BY_INSTRUMENT_NOT_FOUND));
    }

    // 헬퍼 메서드 - 악기 목록이 비어있는지 확인
    private boolean hasNoInstruments(CreateSongRequestDTO requestDTO) {
        return requestDTO.getInstruments() == null || requestDTO.getInstruments().isEmpty();
    }

    // 헬퍼 메서드 - 악기 버전 목록 추가
    private void addInstrumentVersions(Song song, List<CreateSongRequestDTOInstrumentsInner> instruments) {
        for (CreateSongRequestDTOInstrumentsInner instrumentData : instruments) {
            addInstrumentVersion(song, instrumentData);
        }
    }

    // 헬퍼 메서드 - 단일 악기 버전 추가
    private void addInstrumentVersion(Song song, CreateSongRequestDTOInstrumentsInner instrumentData) {
        try {
            // 팩 확인
            SongInstrumentPack pack = findPackById(instrumentData.getSongInstrumentPackId());

            // enum 변환
            Instrument instrument = parseInstrument(instrumentData.getInstrument().getValue());
            Tier tier = parseTier(instrumentData.getTier().getValue());

            // 팩 일치 확인
            validatePackMatch(pack, tier, instrument);

            // 악기 버전 추가
            song.addInstrumentVersion(
                    instrument,
                    tier,
                    instrumentData.getInstrumentUrl(),
                    pack
            );
        } catch (IllegalArgumentException e) {
            log.error("악기 또는 티어 파싱 오류: {}", e.getMessage());
            throw new ExceptionHandler(ErrorStatus.INVALID_INSTRUMENT);
        } catch (IllegalStateException e) {
            handleDomainException(e);
        }
    }

    // 헬퍼 메서드 - 팩 ID로 찾기
    private SongInstrumentPack findPackById(Long packId) {
        return songInstrumentPackRepository.findById(packId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.SONG_INSTRUMENT_PACK_NOT_FOUND));
    }

    // 헬퍼 메서드 - 팩 일치 확인
    private void validatePackMatch(SongInstrumentPack pack, Tier tier, Instrument instrument) {
        if (pack.getSongPackTier() != tier || pack.getSongPackInstrument() != instrument) {
            log.error("선택한 팩의 티어/악기가 요청과 일치하지 않습니다. 팩: {}/{}, 요청: {}/{}",
                    pack.getSongPackTier(), pack.getSongPackInstrument(), tier, instrument);
            throw new ExceptionHandler(ErrorStatus.PACK_INSTRUMENT_MISMATCH);
        }
    }

    // 헬퍼 메서드 - 도메인 예외 처리
    private void handleDomainException(IllegalStateException e) {
        log.error("도메인 모델 제약 조건 위반: {}", e.getMessage());
        if (e.getMessage().contains("이미 동일한 악기와 티어 조합")) {
            throw new ExceptionHandler(ErrorStatus.DUPLICATE_INSTRUMENT_TIER_COMBINATION);
        } else {
            throw new ExceptionHandler(ErrorStatus.PACK_INSTRUMENT_MISMATCH);
        }
    }
}