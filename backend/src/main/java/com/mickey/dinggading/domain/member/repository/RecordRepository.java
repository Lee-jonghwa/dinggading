package com.mickey.dinggading.domain.member.repository;

import com.mickey.dinggading.domain.member.model.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mickey.dinggading.domain.member.model.entity.Record;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findByMember(Member member);
}