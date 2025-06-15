package org.burgas.chatservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.dto.ChatResponse;
import org.burgas.chatservice.dto.IdentityPrincipal;
import org.burgas.chatservice.entity.Chat;
import org.burgas.chatservice.handler.WebClientHandler;
import org.burgas.chatservice.mapper.ChatMapper;
import org.burgas.chatservice.repository.ChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = SUPPORTS)
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final WebClientHandler webClientHandler;

    public Flux<ChatResponse> findChatsByIdentityId(String identityId, String authValue) {
        return webClientHandler.getPrincipal(authValue)
                .flux()
                .flatMap(
                        identityPrincipal -> {
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    Objects.equals(identityPrincipal.getId(), Long.valueOf(identityId))
                            ) {
                                return chatRepository.findChatsByIdentityId(Long.valueOf(identityId))
                                        .flatMap(chat -> chatMapper.toChatResponse(Mono.just(chat), authValue))
                                        .log("CHAT_SERVICE::findByIdentityId");
                            } else
                                return Flux.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }

    public Mono<ChatResponse> findById(String chatId, String identityId, String authValue) {
        return webClientHandler.getPrincipal(authValue)
                .filter(
                        identityPrincipal -> identityPrincipal.getAuthenticated() &&
                                             identityPrincipal.getId() == Long.parseLong(identityId)
                )
                .flatMap(
                        identityPrincipal -> chatRepository.findById(Long.valueOf(chatId))
                                .flatMap(chat -> chatMapper.toChatResponse(Mono.just(chat), authValue))
                                .log("CHAT_SERVICE::findById")
                )
                .switchIfEmpty(
                        Mono.error(new RuntimeException("Пользователь не авторизован и не имеет прав доступа"))
                );
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED, rollbackFor = Exception.class
    )
    public Mono<String> deleteChatById(String chatId,  String authValue) {
        return Mono.zip(
                webClientHandler.getPrincipal(authValue), chatRepository.findById(Long.valueOf(chatId))
        )
                .flatMap(
                        objects -> {
                            IdentityPrincipal identityPrincipal = objects.getT1();
                            Chat chat = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    (
                                            Objects.equals(identityPrincipal.getId(), chat.getSenderId())
                                            ||
                                            Objects.equals(identityPrincipal.getId(), chat.getReceiverId())
                                    )
                            ) {
                                return chatRepository.delete(chat)
                                        .then(Mono.just("Чат успешно удален"))
                                        .log("CHAT_SERVICE::deleteChatById::Success");
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }
}
