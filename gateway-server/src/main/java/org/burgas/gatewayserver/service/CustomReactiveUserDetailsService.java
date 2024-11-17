package org.burgas.gatewayserver.service;

import lombok.RequiredArgsConstructor;
import org.burgas.gatewayserver.handler.WebClientHandler;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {

    private final WebClientHandler webClientHandler;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return webClientHandler.getIdentityByUsername(username).flatMap(Mono::justOrEmpty);
    }
}
