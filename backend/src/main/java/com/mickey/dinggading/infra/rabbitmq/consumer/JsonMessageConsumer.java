package com.mickey.dinggading.infra.rabbitmq.consumer;

import com.mickey.dinggading.domain.attempt.service.AttemptService;
import com.mickey.dinggading.infra.rabbitmq.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JsonMessageConsumer {

    private final AttemptService attemptService;

    @RabbitListener(queues = "${rabbitmq.audio-analysis-response-queue.name}")
    public void receiveJsonMessage(MessageDTO messageDto) {
        log.info("Received JSON message: {}", messageDto);
        Long attemptId = messageDto.getAttemptId();
        Integer beatScore = messageDto.getBeat_score();
        Integer tuneScore = messageDto.getTune_score();
        Integer toneScore = messageDto.getTone_score();

        attemptService.updateAttemptScore(
                attemptId,
                beatScore,
                tuneScore,
                toneScore
        );
        // SSE 요청
    }
}