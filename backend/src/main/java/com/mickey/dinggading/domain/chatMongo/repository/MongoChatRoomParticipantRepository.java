package com.mickey.dinggading.domain.chatMongo.repository;

import com.mickey.dinggading.domain.chatMongo.model.entity.ChatRoomParticipantMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MongoChatRoomParticipantRepository extends MongoRepository<ChatRoomParticipantMongo, String> {
}
