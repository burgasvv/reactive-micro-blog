package org.burgas.chatservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.dto.IdentityPrincipal;
import org.burgas.chatservice.dto.MessageRequest;
import org.burgas.chatservice.handler.WebClientHandler;
import org.burgas.chatservice.kafka.KafkaProducer;
import org.burgas.chatservice.mapper.ChatMapper;
import org.burgas.chatservice.mapper.MessageMapper;
import org.burgas.chatservice.repository.ChatRepository;
import org.burgas.chatservice.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = SUPPORTS)
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ChatRepository chatRepository;
    private final KafkaProducer kafkaProducer;
    private final WebClientHandler webClientHandler;
    private final ChatMapper chatMapper;

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED, rollbackFor = Exception.class
    )
    public Mono<String> sendPrivateMessage(Mono<MessageRequest> messageRequestMono, String authValue) {
        return Mono.zip(messageRequestMono, webClientHandler.getPrincipal(authValue))
                .flatMap(
                        objects -> {
                            MessageRequest messageRequest = objects.getT1();
                            IdentityPrincipal identityPrincipal = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    Objects.equals(identityPrincipal.getId(), messageRequest.getSenderId())
                            ) {
                                return kafkaProducer.sendPrivateMessage(
                                        chatMapper.toChat(Mono.just(messageRequest))
                                                .flatMap(chatRepository::save)
                                                .flatMap(
                                                        chat -> messageMapper.toMessage(
                                                                        Mono.just(messageRequest), Mono.just(chat)
                                                                )
                                                                .flatMap(messageRepository::save)
                                                                .flatMap(
                                                                        message -> messageMapper.toMessageResponse(
                                                                                Mono.just(message), authValue
                                                                        )
                                                                )
                                                )
                                )
                                        .then(
                                                Mono.just("Сообщение успешно отправлено пользователю с идентификатором "
                                                        + messageRequest.getReceiverId())
                                        )
                                        .log("MESSAGE_SERVICE::sendPrivateMessage");
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }
}
