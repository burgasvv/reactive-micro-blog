package org.burgas.communityservice.handler;

import lombok.RequiredArgsConstructor;
import org.burgas.communityservice.dto.CommunityRequest;
import org.burgas.communityservice.dto.CommunityResponse;
import org.burgas.communityservice.dto.IdentityCommunityRequest;
import org.burgas.communityservice.service.CommunityService;
import org.burgas.communityservice.service.IdentityCommunityService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class CommunityHandler {

    private final CommunityService communityService;
    private final IdentityCommunityService identityCommunityService;

    public Mono<ServerResponse> handleFindById(ServerRequest request) {
        return ServerResponse.ok().body(
                communityService.findById(request.pathVariable("community-id")), CommunityResponse.class
        );
    }

    public Mono<ServerResponse> handleFindCommunitiesByIdentityId(ServerRequest request) {
        String authValue = request.headers().firstHeader(AUTHORIZATION);
        return ServerResponse.ok().body(
                communityService.findCommunitiesByIdentityId(
                        request.pathVariable("identity-id"), authValue
                ),
                CommunityResponse.class
        );
    }

    public Mono<ServerResponse> handleCreateCommunityWallByCommunityId(ServerRequest request) {
        return ServerResponse.ok().body(
                communityService.createCommunityWallByCommunityId(
                        request.bodyToMono(CommunityRequest.class),
                        request.queryParam("identityId").orElse(null),
                        request.headers().firstHeader(AUTHORIZATION)
                ), String.class
        );
    }

    public Mono<ServerResponse> handleCreateCommunity(ServerRequest request) {
        String authValue = request.headers().firstHeader(AUTHORIZATION);
        return ServerResponse.ok().body(
                communityService.createCommunity(
                        request.bodyToMono(CommunityRequest.class),
                        request.queryParam("identityId").orElse(null), authValue
                ),
                CommunityResponse.class
        );
    }

    public Mono<ServerResponse> handleJoinTheCommunity(ServerRequest request) {
        String authValue = request.headers().firstHeader(AUTHORIZATION);
        return ServerResponse.ok().body(
                communityService.joinTheCommunity(
                        request.bodyToMono(IdentityCommunityRequest.class), authValue
                ),
                String.class
        );
    }

    public Mono<ServerResponse> handleLeaveTheCommunity(ServerRequest request) {
        String authValue = request.headers().firstHeader(AUTHORIZATION);
        return ServerResponse.ok().body(
                communityService.leaveTheCommunity(
                        request.bodyToMono(IdentityCommunityRequest.class), authValue
                ),
                String.class
        );
    }

    public Mono<ServerResponse> handleSendInvitationToCommunityAdministration(ServerRequest request) {
        return ServerResponse.ok().body(
                identityCommunityService.sendInvitationToCommunityAdministration(
                        request.bodyToMono(IdentityCommunityRequest.class), request.headers().firstHeader(AUTHORIZATION)
                ),
                String.class
        );
    }

    public Mono<ServerResponse> handleAcceptInvitationToCommunityAdministration(ServerRequest request) {
        return ServerResponse.ok().body(
                identityCommunityService.acceptInvitationToCommunityAdministration(
                        request.bodyToMono(IdentityCommunityRequest.class),
                        request.headers().firstHeader(AUTHORIZATION)
                ),
                String.class
        );
    }
}
