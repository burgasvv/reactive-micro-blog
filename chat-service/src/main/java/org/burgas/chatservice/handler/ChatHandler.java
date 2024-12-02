package org.burgas.chatservice.handler;

import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.dto.ChatResponse;
import org.burgas.chatservice.service.ChatService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class ChatHandler {

    private final ChatService chatService;

    public Mono<ServerResponse> handleFindChatsByIdentityId(ServerRequest request) {
        return ServerResponse.ok().body(
                chatService.findChatsByIdentityId(
                        request.pathVariable("identity-id"), request.headers().firstHeader(AUTHORIZATION)
                ),
                ChatResponse.class
        );
    }

    public Mono<ServerResponse> handleFindById(ServerRequest request) {
        return ServerResponse.ok().body(
                chatService.findById(
                        request.pathVariable("chat-id"),
                        request.queryParam("identityId").orElse(null),
                        request.headers().firstHeader(AUTHORIZATION)
                ),
                ChatResponse.class
        );
    }

    public Mono<ServerResponse> handleDeleteChatById(ServerRequest request) {
        return ServerResponse.ok().body(
                chatService.deleteChatById(
                        request.queryParam("chatId").orElse(null),
                        request.headers().firstHeader(AUTHORIZATION)
                ),
                String.class
        );
    }
}
