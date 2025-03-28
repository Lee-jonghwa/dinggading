package com.mickey.dinggading.domain.song.repository;

import com.mickey.dinggading.domain.memberrank.model.Instrument;
import com.mickey.dinggading.domain.memberrank.model.SongByInstrument;
import com.mickey.dinggading.domain.memberrank.model.Tier;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongByInstrumentRepository extends JpaRepository<SongByInstrument, Long> {
    List<SongByInstrument> findBySong_SongId(Long songId);

    // 특정 곡에 특정 악기 버전이 이미 존재하는지 확인
    boolean existsBySong_SongIdAndInstrument(Long songId, Instrument instrument);

    // 특정 곡과 특정 티어와 악기 조합이 이미 존재하는지 확인
    boolean existsBySong_SongIdAndInstrumentAndTier(Long songId, Instrument instrument, Tier tier);
}