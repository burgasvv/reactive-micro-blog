package org.burgas.postservice.handler;

import lombok.RequiredArgsConstructor;
import org.burgas.postservice.dto.IdentityPrincipal;
import org.burgas.postservice.dto.IdentityResponse;
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
                .switchIfEmpty(
                        Mono.error(new RuntimeException("Сервис авторизации не доступен"))
                );
    }

    public Mono<IdentityResponse> getIdentityById(Long identityId, String authValue) {
        return webClient.get()
                .uri("http://localhost:8888/identities/by-id/{identity-id}", identityId)
                .header(AUTHORIZATION, authValue)
                .exchangeToMono(
                        clientResponse -> clientResponse.bodyToMono(IdentityResponse.class)
                )
                .onErrorResume(this::fallBackGetIdentity);
    }

    @SuppressWarnings("unused")
    private Mono<IdentityResponse> fallBackGetIdentity(Throwable throwable) {
        return Mono.error(
                new RuntimeException("Сервис пользователя недоступен")
        );
    }
}
