package org.burgas.chatservice.router;

import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.handler.ChatHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@RequiredArgsConstructor
public class ChatRouter {

    private final ChatHandler chatHandler;

    @Bean
    public RouterFunction<ServerResponse> chats() {
        return RouterFunctions.route()
                .GET("/chats/by-identity/{identity-id}", chatHandler::handleFindChatsByIdentityId)
                .GET("/chats/{chat-id}", chatHandler::handleFindById)
                .build();
    }
}
