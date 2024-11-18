package org.burgas.chatservice.router;

import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.handler.MessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class MessageRouter {

    private final MessageHandler messageHandler;

    @Bean
    public RouterFunction<ServerResponse> messages() {
        return RouterFunctions.route()
                .POST("/messages/send-private-message", messageHandler::handleSendMessage)
                .build();
    }
}
