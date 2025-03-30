package com.mickey.dinggading.infra.rabbitmq.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO implements Serializable {

    private Object content;
    private UUID senderId;
    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return "MessageDto{" +
                "content='" + content + '\'' +
                ", sender='" + senderId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}