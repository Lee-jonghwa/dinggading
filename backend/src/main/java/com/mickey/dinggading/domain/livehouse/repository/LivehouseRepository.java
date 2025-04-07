// 파일 경로: src/main/java/com/mickey/dinggading/domain/livehouse/repository/LivehouseRepository.java
package com.mickey.dinggading.domain.livehouse.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.mickey.dinggading.domain.livehouse.entity.Livehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LivehouseRepository extends JpaRepository<Livehouse, Long> {
    Page<Livehouse> findBySessionIdIn(List<String> sessionIds, Pageable pageable);

    Optional<Livehouse> findBySessionIdInAndHostId(List<String> activeSessions, UUID memberId);

    Optional<Livehouse> findBySessionIdInAndLivehouseId(List<String> activeSessions, Long livehouseId);
}
