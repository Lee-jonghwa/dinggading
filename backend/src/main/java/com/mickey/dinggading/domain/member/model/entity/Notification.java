package com.mickey.dinggading.domain.member.model.entity;

import com.mickey.dinggading.base.BaseEntity;
import com.mickey.dinggading.domain.chatMongo.model.entity.ChatMessageMongo;
import com.mickey.dinggading.model.NotificationDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Notification")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;
    
    @Column(name = "message", nullable = false)
    private String message;
    
    @Column(name = "read_or_not", nullable = false)
    private Boolean readOrNot;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Comment("알림 타입")
    private NotificationDTO.TypeEnum type;

    @Column(name = "chat_room_id", nullable = false)
    @Comment("채팅방 ID")
    private String chatRoomId;

//    @Column(name = "accept_url")
//    @Comment("수락 시 사용할 URL")
//    private String acceptUrl;
//
//    @Column(name = "reject_url")
//    @Comment("거절 시 사용할 URL")
//    private String rejectUrl;
//
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성된 날자")
    private LocalDateTime createdAt;

    public enum NotificationType {
        CHAT, FOLLOW, RECRUITMENT
    }
    
    // Update methods
    public void markAsRead() {
        this.readOrNot = true;
    }

    public Notification (String chatRoomId, String message, Member sender, Member receiver, NotificationDTO.TypeEnum type, boolean readOrNot) {
        this.chatRoomId = chatRoomId;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.type = type;
        this.readOrNot = readOrNot;
        this.createdAt = LocalDateTime.now();
    }
}