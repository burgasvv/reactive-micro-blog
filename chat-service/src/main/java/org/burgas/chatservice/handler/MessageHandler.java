package org.burgas.chatservice.handler;

import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.dto.MessageRequest;
import org.burgas.chatservice.service.MessageService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public Mono<ServerResponse> handleServerSendMessageEvent(ServerRequest ignoredRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(
                        Flux.interval(Duration.ofSeconds(5))
                                .flatMap(
                                        aLong -> Flux.just(
                                                ServerSentEvent.<String>builder()
                                                        .id(Long.toString(aLong))
                                                        .event("New notification event message")
                                                        .data(LocalDateTime.now().format(
                                                                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                                                        ))
                                                        .build()
                                        )
                                ),
                        ServerSentEvent.class
                );
    }

    public Mono<ServerResponse> handleDeleteMessage(ServerRequest request) {
        return ServerResponse.ok().body(
                messageService.deleteMessage(
                        request.queryParam("messageId").orElse(null),
                        request.headers().firstHeader(AUTHORIZATION)
                ),
                String.class
        );
    }
}
