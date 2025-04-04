// 파일 경로: src/main/java/com/mickey/dinggading/domain/livehouse/repository/LivehouseRepository.java
package com.mickey.dinggading.domain.livehouse.repository;

import com.mickey.dinggading.domain.livehouse.entity.Livehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivehouseRepository extends JpaRepository<Livehouse, Long> {
    Page<Livehouse> findByStatus(Livehouse.LivehouseStatus status, Pageable pageable);
}
