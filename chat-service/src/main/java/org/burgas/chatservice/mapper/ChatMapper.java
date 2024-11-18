package org.burgas.chatservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.dto.ChatResponse;
import org.burgas.chatservice.dto.IdentityResponse;
import org.burgas.chatservice.dto.MessageRequest;
import org.burgas.chatservice.dto.MessageResponse;
import org.burgas.chatservice.entity.Chat;
import org.burgas.chatservice.handler.WebClientHandler;
import org.burgas.chatservice.repository.ChatRepository;
import org.burgas.chatservice.repository.MessageRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMapper {

    private final ChatRepository chatRepository;
    private final WebClientHandler webClientHandler;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public Mono<Chat> toChat(Mono<MessageRequest> messageRequestMono) {
        return messageRequestMono.flatMap(
                messageRequest -> {
                    Long chatId = messageRequest.getChatId() == null ? 0L : messageRequest.getChatId();
                    return chatRepository.findById(chatId).mapNotNull(chat -> chat)
                            .flatMap(
                                    chat -> Mono.fromCallable(
                                            () -> Chat.builder()
                                                    .id(chat.getId())
                                                    .senderId(chat.getSenderId())
                                                    .receiverId(chat.getReceiverId())
                                                    .isNew(false)
                                                    .build()
                                    )
                            )
                            .switchIfEmpty(
                                    Mono.fromCallable(
                                            () -> Chat.builder()
                                                    .id(messageRequest.getChatId())
                                                    .senderId(messageRequest.getSenderId())
                                                    .receiverId(messageRequest.getReceiverId())
                                                    .isNew(true)
                                                    .build()
                                    )
                            );
                }
        );
    }

    public Mono<ChatResponse> toChatResponse(Mono<Chat> chatMono, String authValue) {
        return chatMono.flatMap(
                chat -> Mono.zip(
                        webClientHandler.getIdentityById(chat.getSenderId(), authValue),
                        webClientHandler.getIdentityById(chat.getReceiverId(), authValue),
                        messageRepository.findMessagesByChatId(chat.getId()).flatMap(
                                message -> messageMapper.toMessageResponse(Mono.just(message), authValue)
                        ).collectList()
                )
                        .flatMap(
                                objects -> {
                                    IdentityResponse sender = objects.getT1();
                                    IdentityResponse receiver = objects.getT2();
                                    List<MessageResponse> messages = objects.getT3();
                                    return Mono.fromCallable(
                                            () -> ChatResponse.builder()
                                                    .id(chat.getId())
                                                    .sender(sender)
                                                    .receiver(receiver)
                                                    .messages(messages)
                                                    .build()
                                    );
                                }
                        )
        );
    }
}
