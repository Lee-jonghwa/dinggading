package com.mickey.dinggading.domain.memberrank.repository;

import com.mickey.dinggading.domain.memberrank.model.Instrument;
import com.mickey.dinggading.domain.memberrank.model.MemberRank;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRankRepository extends JpaRepository<MemberRank, Long> {

    /**
     * 회원 ID로 해당 회원의 모든 악기별 랭크 정보를 조회
     *
     * @param memberId 조회할 회원 ID
     * @return 회원의 악기별 랭크 정보 리스트
     */
    List<MemberRank> findByMemberMemberId(UUID memberId);

    /**
     * 회원 ID와 악기로 특정 랭크 정보 조회
     *
     * @param memberId   조회할 회원 ID
     * @param instrument 조회할 악기 타입
     * @return 특정 회원의 특정 악기에 대한 랭크 정보
     */
    @Query("SELECT mr FROM MemberRank mr WHERE mr.member.memberId = :memberId AND mr.instrument = :instrument")
    Optional<MemberRank> findByMemberMemberIdAndInstrument(UUID memberId, Instrument instrument);

    /**
     * 회원 ID와 악기로 존재 여부 확인
     *
     * @param memberId   조회할 회원 ID
     * @param instrument 조회할 악기 타입
     * @return 존재 여부
     */
    boolean existsByMemberMemberIdAndInstrument(UUID memberId, Instrument instrument);
}