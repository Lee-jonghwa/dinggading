package com.mickey.dinggading.domain.song.repository;

import com.mickey.dinggading.domain.memberrank.model.Instrument;
import com.mickey.dinggading.domain.memberrank.model.SongInstrumentPack;
import com.mickey.dinggading.domain.memberrank.model.Tier;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SongInstrumentPackRepository extends JpaRepository<SongInstrumentPack, Long> {
    // 티어와 악기로 팩 찾기
    Optional<SongInstrumentPack> findBySongPackTierAndSongPackInstrument(Tier tier, Instrument instrument);

    // 티어로 팩 찾기
    Page<SongInstrumentPack> findAllBySongPackTier(Tier tier, Pageable pageable);

    // 악기로 팩 찾기
    Page<SongInstrumentPack> findAllBySongPackInstrument(Instrument instrument, Pageable pageable);

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
}