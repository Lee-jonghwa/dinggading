package com.mickey.dinggading.domain.membermatching.repository;

import com.mickey.dinggading.domain.memberrank.model.Instrument;
import com.mickey.dinggading.domain.memberrank.model.SongInstrumentPack;
import com.mickey.dinggading.domain.memberrank.model.Tier;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * SongInstrumentPack 엔티티에 대한 데이터 접근 인터페이스
 */
@Repository
public interface SongInstrumentPackRepository extends JpaRepository<SongInstrumentPack, Long> {

//    /**
//     * 티어별 곡 팩 목록을 페이징 조회
//     *
//     * @param tier     티어
//     * @param pageable 페이징 정보
//     * @return 곡 팩 페이지
//     */
//    Page<SongInstrumentPack> findBySongPackTier(Tier tier, Pageable pageable);
//
//    /**
//     * 악기별 곡 팩 목록을 페이징 조회
//     *
//     * @param instrument 악기
//     * @param pageable   페이징 정보
//     * @return 곡 팩 페이지
//     */
//    Page<SongInstrumentPack> findBySongPackInstrument(Instrument instrument, Pageable pageable);
//

    /**
     * 악기와 티어로 곡 팩 목록을 조회
     *
     * @param instrument 악기
     * @param tier       티어
     * @return 곡 팩 목록
     */
    @Query("SELECT sip FROM SongInstrumentPack sip WHERE sip.songPackInstrument = :instrument AND sip.songPackTier = :tier")
    List<SongInstrumentPack> findByInstrumentAndTier(
            @Param("instrument") Instrument instrument,
            @Param("tier") Tier tier);
//
//    /**
//     * 악기와 티어로 곡 팩 목록을 페이징 조회
//     *
//     * @param instrument 악기
//     * @param tier       티어
//     * @param pageable   페이징 정보
//     * @return 곡 팩 페이지
//     */
//    Page<SongInstrumentPack> findBySongPackInstrumentAndSongPackTier(Instrument instrument, Tier tier,
//                                                                     Pageable pageable);

    /**
     * 악기와 티어 목록으로 곡 팩 목록을 조회
     *
     * @param instrument 악기
     * @param tiers      티어 목록
     * @return 곡 팩 목록
     */
    @Query("SELECT sip FROM SongInstrumentPack sip WHERE sip.songPackInstrument = :instrument AND sip.songPackTier IN :tiers")
    List<SongInstrumentPack> findByInstrumentAndTierIn(@Param("instrument") Instrument instrument,
                                                       @Param("tiers") List<Tier> tiers);

//    /**
//     * 악기와 티어 목록으로 곡 팩 목록을 페이징 조회
//     *
//     * @param instrument 악기
//     * @param tiers      티어 목록
//     * @param pageable   페이징 정보
//     * @return 곡 팩 페이지
//     */
//    Page<SongInstrumentPack> findBySongPackInstrumentAndSongPackTierIn(Instrument instrument, List<Tier> tiers,
//                                                                       Pageable pageable);
//
//    /**
//     * 특정 티어 이상의 곡 팩 목록을 조회
//     *
//     * @param tier 기준 티어
//     * @return 곡 팩 목록
//     */
//    @Query("SELECT sip FROM SongInstrumentPack sip WHERE sip.songPackTier >= :tier")
//    List<SongInstrumentPack> findByTierGreaterThanEqual(@Param("tier") Tier tier);
//
//    /**
//     * 특정 티어 이하의 곡 팩 목록을 조회
//     *
//     * @param tier 기준 티어
//     * @return 곡 팩 목록
//     */
//    @Query("SELECT sip FROM SongInstrumentPack sip WHERE sip.songPackTier <= :tier")
//    List<SongInstrumentPack> findByTierLessThanEqual(@Param("tier") Tier tier);
//
//    /**
//     * 특정 곡 ID가 포함된 팩 목록을 조회
//     *
//     * @param songId 곡 ID
//     * @return 곡 팩 목록
//     */
//    @Query("SELECT DISTINCT sip FROM SongInstrumentPack sip JOIN sip.songs sbi WHERE sbi.songId = :songId")
//    List<SongInstrumentPack> findBySongId(@Param("songId") Long songId);
//
//    /**
//     * 곡 팩 이름으로 검색
//     *
//     * @param packName 팩 이름 (부분 일치)
//     * @param pageable 페이징 정보
//     * @return 곡 팩 페이지
//     */
//    Page<SongInstrumentPack> findByPackNameContaining(String packName, Pageable pageable);
//
//    /**
//     * 악기별 각 티어의 대표 곡 팩을 조회 (각 티어당 하나씩)
//     *
//     * @param instrument 악기
//     * @return 티어별 대표 곡 팩 목록
//     */
//    @Query("SELECT sip FROM SongInstrumentPack sip WHERE sip.songPackInstrument = :instrument " +
//            "GROUP BY sip.songPackTier ORDER BY sip.songPackTier")
//    List<SongInstrumentPack> findRepresentativePacksByInstrument(@Param("instrument") Instrument instrument);
//
//    /**
//     * 특정 수 이상의 곡이 포함된 곡 팩 목록을 조회
//     *
//     * @param songCount 최소 곡 수
//     * @return 곡 팩 목록
//     */
//    @Query("SELECT sip FROM SongInstrumentPack sip WHERE SIZE(sip.songs) >= :songCount")
//    List<SongInstrumentPack> findPacksWithMinimumSongs(@Param("songCount") int songCount);
}