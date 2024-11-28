package org.burgas.chatservice.handler;

import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.dto.MessageRequest;
import org.burgas.chatservice.service.MessageService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class MessageHandler {

    private final MessageService messageService;

    public Mono<ServerResponse> handleSendMessage(ServerRequest request) {
        return ServerResponse.ok()
                .body(
                        messageService.sendPrivateMessage(
                                request.bodyToMono(MessageRequest.class),
                                request.headers().firstHeader(AUTHORIZATION)
                        ),
                        String.class
                );
    }
}
