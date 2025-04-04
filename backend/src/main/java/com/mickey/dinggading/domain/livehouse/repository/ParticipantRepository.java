// 파일 경로: src/main/java/com/mickey/dinggading/domain/livehouse/repository/ParticipantRepository.java
package com.mickey.dinggading.domain.livehouse.repository;

import com.mickey.dinggading.domain.livehouse.entity.Livehouse;
import com.mickey.dinggading.domain.livehouse.entity.Participant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByLivehouse(Livehouse livehouse);
}