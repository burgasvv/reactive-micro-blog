package org.burgas.chatservice.kafka;

import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.dto.MessageResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    @KafkaListener(groupId = "chat-topic-group-id", topics = "chat-topic")
    public Mono<MessageResponse> receivePrivateMessage(Mono<MessageResponse> messageResponseMono) {
        return messageResponseMono.doOnNext(
                messageResponse -> System.out.println("Received message: " + messageResponse)
        );
    }
}
