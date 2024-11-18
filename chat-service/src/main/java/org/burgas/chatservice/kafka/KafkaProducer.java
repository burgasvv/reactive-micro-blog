package org.burgas.chatservice.kafka;

import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.dto.MessageResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, MessageResponse> kafkaTemplate;

    public Mono<Void> sendPrivateMessage(Mono<MessageResponse> messageResponseMono) {
        return messageResponseMono.flatMap(
                messageResponse -> {
                    kafkaTemplate.send("chat-topic", String.valueOf(messageResponse.getChatId()), messageResponse);
                    return Mono.empty();
                }
        );
    }
}
