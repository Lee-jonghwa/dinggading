package com.mickey.dinggading.domain.song.repository;

import com.mickey.dinggading.domain.memberrank.model.Instrument;
import com.mickey.dinggading.domain.memberrank.model.Song;
import com.mickey.dinggading.domain.memberrank.model.Tier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    // 특정 악기가 포함된 곡 찾기
    @Query("SELECT DISTINCT s FROM Song s JOIN s.songByInstruments sbi WHERE sbi.instrument = :instrument")
    Page<Song> findAllByInstrument(@Param("instrument") Instrument instrument, Pageable pageable);

    // 특정 티어가 포함된 곡 찾기
    @Query("SELECT DISTINCT s FROM Song s JOIN s.songByInstruments sbi WHERE sbi.tier = :tier")
    Page<Song> findAllByTier(@Param("tier") Tier tier, Pageable pageable);

    // 제목으로 검색
    Page<Song> findByTitleContaining(String title, Pageable pageable);
}