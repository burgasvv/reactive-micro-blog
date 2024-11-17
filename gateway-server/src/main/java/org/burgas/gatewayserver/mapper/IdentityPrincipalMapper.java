package org.burgas.gatewayserver.mapper;

import org.burgas.gatewayserver.dto.IdentityPrincipal;
import org.burgas.gatewayserver.dto.IdentityResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class IdentityPrincipalMapper {

    public Mono<IdentityPrincipal> toIdentityPrincipal(
            Mono<IdentityResponse> identityResponseMono, Boolean authenticated
    ) {
        return identityResponseMono.flatMap(
                identityResponse -> Mono.fromCallable(
                        () -> IdentityPrincipal.builder()
                                .id(identityResponse.getId())
                                .username(identityResponse.getUsername())
                                .password(identityResponse.getPassword())
                                .authorities(
                                        identityResponse.getAuthorities()
                                                .stream().map(GrantedAuthority::getAuthority)
                                                .toList()
                                )
                                .enabled(identityResponse.getEnabled())
                                .authenticated(authenticated)
                                .build()
                )
        );
    }
}
