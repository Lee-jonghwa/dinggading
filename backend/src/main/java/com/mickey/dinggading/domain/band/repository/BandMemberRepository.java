package com.mickey.dinggading.domain.band.repository;

import com.mickey.dinggading.domain.band.model.entity.BandMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BandMemberRepository extends JpaRepository<BandMember, Long> {

}