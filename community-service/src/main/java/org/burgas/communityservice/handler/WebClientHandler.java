package org.burgas.communityservice.handler;

import lombok.RequiredArgsConstructor;
import org.burgas.communityservice.dto.IdentityPrincipal;
import org.burgas.communityservice.dto.PostRequest;
import org.burgas.communityservice.dto.PostResponse;
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
                .onErrorResume(Mono::error);
    }

    public Mono<PostResponse> createOrUpdatePost(Mono<PostRequest> postRequestMono, String authValue) {
        return webClient.post()
                .uri("http://localhost:90000/posts/create")
                .body(postRequestMono, PostRequest.class)
                .header(AUTHORIZATION, authValue)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(PostResponse.class));
    }
}
