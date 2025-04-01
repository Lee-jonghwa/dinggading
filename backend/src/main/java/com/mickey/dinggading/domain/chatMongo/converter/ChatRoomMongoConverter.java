package com.mickey.dinggading.domain.chatMongo.converter;

import com.mickey.dinggading.domain.chatMongo.model.entity.ChatMessageMongo;
import com.mickey.dinggading.domain.chatMongo.model.entity.ChatNoticeMongo;
import com.mickey.dinggading.domain.chatMongo.model.entity.ChatRoomMongo;
import com.mickey.dinggading.model.ChatMessageDTO;
import com.mickey.dinggading.model.ChatNoticeDTO;
import com.mickey.dinggading.model.ChatRoomDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatRoomMongoConverter {

    public ChatRoomDTO toDto(ChatRoomMongo chatroom) {
        return ChatRoomDTO.builder()
                .chatroomId(chatroom.getId())
                .roomType(chatroom.getRoomType())
                .latestChat(chatroom.getLatestChat())
                .latestChatDate(chatroom.getLatestChatDate() != null ?
                        chatroom.getLatestChatDate() : null)
                .participantCount(chatroom.getParticipants().size())
                .build();
    }

    public ChatMessageDTO toMessageDto(ChatMessageMongo chatMessage, UUID memberId) {
        if (chatMessage == null) {
            return null;
        }

        return ChatMessageDTO.builder()
                .chatMessageId(chatMessage.getMessageId())
                .chatRoomId(chatMessage.getChatRoomId())
                .senderId(memberId)
                .senderNickname(chatMessage.getMemberNickname())
                .senderProfileUrl(chatMessage.getMemberProfileUrl())
                .message(chatMessage.getMessage())
                .messageType(chatMessage.getMessageType())
                .readCount(chatMessage.getReadCount())
                .build();
    }

    public ChatNoticeDTO toNoticeDto(ChatNoticeMongo chatNotice, UUID memberId) {
        return ChatNoticeDTO.builder()
                .noticeId(chatNotice.getId())
                .chatRoomId(chatNotice.getChatRoomId())
                .content(chatNotice.getContent())
                .authorId(memberId)
                .pinned(chatNotice.getPinned())
                .createdAt(chatNotice.getCreatedAt())
                .updatedAt(chatNotice.getUpdatedAt())
                .build();
    }
}
