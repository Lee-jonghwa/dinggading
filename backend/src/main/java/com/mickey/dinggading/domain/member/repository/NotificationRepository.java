package com.mickey.dinggading.domain.member.repository;

import com.mickey.dinggading.domain.member.model.entity.Member;
import com.mickey.dinggading.domain.member.model.entity.Notification;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByReceiver(Member receiver, Pageable pageable);
    Page<Notification> findByReceiverAndType(Member receiver, Notification.NotificationType type, Pageable pageable);

}