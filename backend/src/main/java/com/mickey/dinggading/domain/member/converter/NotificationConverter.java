package com.mickey.dinggading.domain.member.converter;

import com.mickey.dinggading.domain.member.model.entity.Notification;
import com.mickey.dinggading.model.NotificationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
public class NotificationConverter {

    public NotificationDTO fromEntity(Notification entity, String tierAndInstrument) {
        return NotificationDTO.builder()
            .tierAndInstrument(tierAndInstrument)
            .notificationId(entity.getNotificationId())
            .attemptId(entity.getAttemptId())
            .isSuccess(entity.getIsSuccess())
            .senderId(entity.getSender().getMemberId())
            .receiverId(entity.getReceiver().getMemberId())
            .senderNickname(entity.getSender().getNickname())
            .senderProfileUrl(entity.getSender().getProfileImgUrl())
            .message(entity.getMessage())
            .type(entity.getType())
            .readOrNot(entity.getReadOrNot())
            .createdAt(LocalDateTime.now())
            .build();
    }

    public NotificationDTO fromEntity(Notification entity) {
        return NotificationDTO.builder()
            .notificationId(entity.getNotificationId())
            .chatRoomId(entity.getChatRoomId())
            .senderId(entity.getSender().getMemberId())
            .receiverId(entity.getReceiver().getMemberId())
            .senderNickname(entity.getSender().getNickname())
            .senderProfileUrl(entity.getSender().getProfileImgUrl())
            .message(entity.getMessage())
            .type(entity.getType())
            .readOrNot(entity.getReadOrNot())
            .createdAt(LocalDateTime.now())
            .build();
    }
}
