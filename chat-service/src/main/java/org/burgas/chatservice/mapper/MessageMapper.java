package org.burgas.chatservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.dto.IdentityResponse;
import org.burgas.chatservice.dto.MessageRequest;
import org.burgas.chatservice.dto.MessageResponse;
import org.burgas.chatservice.entity.Chat;
import org.burgas.chatservice.entity.Message;
import org.burgas.chatservice.handler.CryptHandler;
import org.burgas.chatservice.handler.WebClientHandler;
import org.burgas.chatservice.repository.MessageRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final MessageRepository messageRepository;
    private final WebClientHandler webClientHandler;
    private final CryptHandler cryptHandler;

    public Mono<Message> toMessage(Mono<MessageRequest> messageRequestMono, Mono<Chat> chatMono) {
        return Mono.zip(messageRequestMono, chatMono)
                .flatMap(
                        objects -> {
                            MessageRequest messageRequest = objects.getT1();
                            Chat chat = objects.getT2();
                            {
                                Long messageId = messageRequest.getId() == null ? 0L : messageRequest.getId();
                                return messageRepository.findById(messageId)
                                        .mapNotNull(message -> message)
                                        .flatMap(
                                                message -> Mono.fromCallable(
                                                        () -> Message.builder()
                                                                .id(messageRequest.getId())
                                                                .chatId(chat.getId())
                                                                .senderId(messageRequest.getSenderId())
                                                                .receiverId(messageRequest.getReceiverId())
                                                                .content(cryptHandler.encrypt(messageRequest.getContent()))
                                                                .receivedAt(message.getReceivedAt())
                                                                .isNew(false)
                                                                .build()
                                                )
                                        )
                                        .switchIfEmpty(
                                                Mono.fromCallable(
                                                        () -> Message.builder()
                                                                .id(messageRequest.getId())
                                                                .chatId(chat.getId())
                                                                .senderId(messageRequest.getSenderId())
                                                                .receiverId(messageRequest.getReceiverId())
                                                                .content(cryptHandler.encrypt(messageRequest.getContent()))
                                                                .receivedAt(LocalDateTime.now())
                                                                .isNew(true)
                                                                .build()
                                                )
                                        );
                            }
                        }
                );
    }

    public Mono<MessageResponse> toMessageResponse(Mono<Message> messageMono, String authValue) {
        return messageMono.flatMap(
                message -> Mono.zip(
                        webClientHandler.getIdentityById(message.getSenderId(), authValue),
                        webClientHandler.getIdentityById(message.getReceiverId(), authValue)
                )
                        .flatMap(
                                objects -> {
                                    IdentityResponse sender = objects.getT1();
                                    IdentityResponse receiver = objects.getT2();
                                    return Mono.fromCallable(
                                            () -> MessageResponse.builder()
                                                    .id(message.getId())
                                                    .chatId(message.getChatId())
                                                    .sender(sender)
                                                    .receiver(receiver)
                                                    .content(cryptHandler.decrypt(message.getContent()))
                                                    .receivedAt(
                                                            message.getReceivedAt().format(
                                                                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                                                            )
                                                    )
                                                    .build()
                                    );
                                }
                        )
        );
    }
}
