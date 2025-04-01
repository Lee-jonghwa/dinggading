package com.mickey.dinggading.domain.chatMongo.repository;

import com.mickey.dinggading.domain.chatMongo.model.entity.ChatRoomSettingMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MongoChatRoomSettingRepository extends MongoRepository<ChatRoomSettingMongo, String> {
    List<ChatRoomSettingMongo> findAllByChatRoomId(String roomId);
}
