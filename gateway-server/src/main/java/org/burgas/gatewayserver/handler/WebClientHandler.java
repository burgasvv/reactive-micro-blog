package org.burgas.gatewayserver.handler;

import lombok.RequiredArgsConstructor;
import org.burgas.gatewayserver.dto.IdentityResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WebClientHandler {

    private final WebClient webClient;

    public Mono<IdentityResponse> getIdentityByUsername(String username) {
        return webClient.get()
                .uri("http://localhost:8888/identities/{username}", username)
                .exchangeToMono(
                        clientResponse -> clientResponse.bodyToMono(IdentityResponse.class)
                )
                .onErrorResume(this::fallBackGetIdentityByUsername);
    }

    private Mono<IdentityResponse> fallBackGetIdentityByUsername(Throwable throwable) {
        return Mono.error(
                new RuntimeException("Пользовательский сервер недоступен")
        );
    }
}
