package org.burgas.chatservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.dto.ChatResponse;
import org.burgas.chatservice.mapper.ChatMapper;
import org.burgas.chatservice.repository.ChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = SUPPORTS)
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

    public Flux<ChatResponse> findChatsByIdentityId(String identityId, String authValue) {
        return chatRepository.findChatsByIdentityId(Long.valueOf(identityId))
                .flatMap(chat -> chatMapper.toChatResponse(Mono.just(chat), authValue))
                .log("CHAT_SERVICE::findByIdentityId");
    }
}
