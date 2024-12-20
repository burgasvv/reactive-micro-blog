package org.burgas.identityservice.handler;

import lombok.RequiredArgsConstructor;
import org.burgas.identityservice.dto.IdentityRequestCreate;
import org.burgas.identityservice.dto.IdentityRequestUpdate;
import org.burgas.identityservice.dto.IdentityResponse;
import org.burgas.identityservice.service.IdentityService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class IdentityHandler {

    private final IdentityService identityService;

    public Mono<ServerResponse> handleFindAll(final ServerRequest ignoredRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(identityService.findAll(), IdentityResponse.class);
    }

    public Mono<ServerResponse> handleFindById(final ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        identityService.findById(request.pathVariable("identity-id")),
                        IdentityResponse.class
                );
    }

    public Mono<ServerResponse> handleFindByUsername(final ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        identityService.findByUsername(request.pathVariable("username")),
                        IdentityResponse.class
                );
    }

    public Mono<ServerResponse> handleCreateIdentityWall(final ServerRequest request) {
        return ServerResponse.ok()
                .body(
                        identityService.createIdentityWall(request.bodyToMono(IdentityRequestCreate.class),
                                request.headers().firstHeader(AUTHORIZATION)
                        ),
                        String.class
                );
    }

    public Mono<ServerResponse> handleCreateIdentity(final ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        identityService.createIdentity(
                                request.bodyToMono(IdentityRequestCreate.class),
                                request.headers().firstHeader(AUTHORIZATION)
                        ),
                        IdentityResponse.class
                );
    }

    public Mono<ServerResponse> handleUpdateIdentity(final ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(
                        identityService.updateIdentity(
                                request.bodyToMono(IdentityRequestUpdate.class),
                                request.headers().firstHeader(AUTHORIZATION)
                        ),
                        IdentityResponse.class
                );
    }

    public Mono<ServerResponse> handleDeleteIdentity(final ServerRequest request) {
        return ServerResponse.ok()
                .body(
                        identityService.deleteIdentity(
                                request.pathVariable("identity-id"),
                                request.headers().firstHeader(AUTHORIZATION)
                        ),
                        String.class
                );
    }

    public Mono<ServerResponse> handleFindFriendsByIdentityId(final ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        identityService.findFriendsByIdentityId(
                                request.pathVariable("identity-id"),
                                request.headers().firstHeader(AUTHORIZATION)
                        ),
                        IdentityResponse.class
                );
    }

    public Mono<ServerResponse> handleFindIdentitiesByCommunityId(final ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        identityService.findIdentitiesByCommunityId(
                                request.queryParam("identityId").orElse(null),
                                request.queryParam("communityId").orElse(null),
                                request.headers().firstHeader(AUTHORIZATION)
                        ),
                        IdentityResponse.class
                );
    }
}
