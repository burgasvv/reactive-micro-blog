package org.burgas.gatewayserver.handler;

import lombok.RequiredArgsConstructor;
import org.burgas.gatewayserver.dto.IdentityPrincipal;
import org.burgas.gatewayserver.dto.IdentityResponse;
import org.burgas.gatewayserver.mapper.IdentityPrincipalMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationHandler {

    private final IdentityPrincipalMapper identityPrincipalMapper;

    public Mono<ServerResponse> getPrincipal(ServerRequest ignoredRequest) {
        return ReactiveSecurityContextHolder.getContext()
                .mapNotNull(SecurityContext::getAuthentication)
                .flatMap(
                        authentication -> ServerResponse.ok().body(
                                identityPrincipalMapper.toIdentityPrincipal(
                                        Mono.just((IdentityResponse) authentication.getPrincipal()), true
                                ),
                                IdentityPrincipal.class
                        )
                )
                .switchIfEmpty(
                        ServerResponse.status(HttpStatus.UNAUTHORIZED).body(
                                Mono.fromCallable(
                                        () -> IdentityPrincipal.builder().username("anonymous").authenticated(false)
                                                .build()
                                ), IdentityPrincipal.class
                        )
                );
    }
}
