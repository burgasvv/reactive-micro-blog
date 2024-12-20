package org.burgas.chatservice.handler;

import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.dto.IdentityPrincipal;
import org.burgas.chatservice.dto.IdentityResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class WebClientHandler {

    private final WebClient webClient;

    public Mono<IdentityPrincipal> getPrincipal(String authValue) {
        return webClient.get()
                .uri("http://localhost:8765/authentication/principal")
                .header(AUTHORIZATION, authValue)
                .exchangeToMono(
                        clientResponse -> clientResponse.bodyToMono(IdentityPrincipal.class)
                )
                .onErrorResume(
                        throwable -> Mono.error(new RuntimeException(throwable.getMessage()))
                );
    }

    public Mono<IdentityResponse> getIdentityById(Long id, String authValue) {
        return webClient.get()
                .uri("http://localhost:8888/identities/by-id/{identity-id}", id)
                .header(AUTHORIZATION, authValue)
                .exchangeToMono(
                        clientResponse -> clientResponse.bodyToMono(IdentityResponse.class)
                )
                .onErrorResume(
                        throwable -> Mono.error(new RuntimeException(throwable.getMessage()))
                );
    }
}
