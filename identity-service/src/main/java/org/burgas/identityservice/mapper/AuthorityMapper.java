package org.burgas.identityservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.identityservice.dto.AuthorityResponse;
import org.burgas.identityservice.entity.Authority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthorityMapper {

    public Mono<AuthorityResponse> toAuthorityResponse(Mono<Authority> authorityMono) {
        return authorityMono
                .flatMap(
                        authority -> Mono.just(
                                AuthorityResponse.builder()
                                        .id(authority.getId())
                                        .name(authority.getName())
                                        .build()
                        )
                );
    }
}
