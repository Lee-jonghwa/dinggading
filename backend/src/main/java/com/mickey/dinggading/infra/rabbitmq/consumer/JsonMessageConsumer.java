package com.mickey.dinggading.infra.rabbitmq.consumer;

import com.mickey.dinggading.domain.attempt.service.AttemptService;
import com.mickey.dinggading.infra.rabbitmq.dto.MessageDTO;
import com.mickey.dinggading.model.AttemptDTO;
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
        AttemptDTO attemptDTO = (AttemptDTO) messageDto.getContent();

        attemptService.updateAttemptScore(
                attemptDTO.getAttemptId(),
                attemptDTO.getBeatScore(),
                attemptDTO.getTuneScore(),
                attemptDTO.getToneScore()
        );
        // SSE 요청
    }
}