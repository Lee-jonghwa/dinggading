package com.mickey.dinggading.domain.chatMongo.repository;

import com.mickey.dinggading.domain.chatMongo.model.entity.ChatMessageMongo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MongoChatMessageRepository extends MongoRepository<ChatMessageMongo, String> {
    Optional<ChatMessageMongo> findTopByChatRoomIdOrderByCreatedAtDesc(String roomId);

    Page<ChatMessageMongo> findByChatRoomId(String id, Pageable sortedPageable);
}
