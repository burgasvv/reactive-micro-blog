package org.burgas.chatservice.kafka;

import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.dto.MessageResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    @KafkaListener(groupId = "chat-topic-group-id", topics = "chat-topic")
    public MessageResponse receivePrivateMessage(MessageResponse messageResponse) {
        System.out.println(messageResponse);
        return messageResponse;
    }
}
