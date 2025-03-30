package com.mickey.dinggading.infra.rabbitmq.consumer;

import com.mickey.dinggading.infra.rabbitmq.dto.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JsonMessageConsumer {

    @RabbitListener(queues = "${rabbitmq.audio-analysis-response-queue.name}")
    public void receiveJsonMessage(MessageDTO messageDto) {
        log.info("Received JSON message: {}", messageDto);
    }
}