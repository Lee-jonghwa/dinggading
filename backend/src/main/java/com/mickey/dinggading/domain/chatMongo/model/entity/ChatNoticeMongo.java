package com.mickey.dinggading.domain.chatMongo.model.entity;

import com.mickey.dinggading.base.BaseEntity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection="chat_notice")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatNoticeMongo {

    @Id
    private String id;

    private String chatRoomId;
    private String content;
    private String authorId;
    private Boolean pinned;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ChatNoticeMongo (String chatRoomId, String content, String authorId, Boolean pinned) {
        this.chatRoomId = chatRoomId;
                this.content= content;
                this.authorId= authorId;
                this.pinned= pinned;
                this.createdAt= LocalDateTime.now();
                this.updatedAt= LocalDateTime.now();
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
    public void updatePinned(Boolean pinned) {
        this.pinned = pinned;
        this.updatedAt = LocalDateTime.now();
    }
}