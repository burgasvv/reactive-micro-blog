package org.burgas.identityservice.handler;

import lombok.RequiredArgsConstructor;
import org.burgas.identityservice.dto.FriendshipNotification;
import org.burgas.identityservice.dto.FriendshipRequest;
import org.burgas.identityservice.service.FriendshipService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class FriendshipHandler {

    private final FriendshipService friendshipService;

    public Mono<ServerResponse> handleFindFriendshipNotificationsByFriendId(ServerRequest request) {
        return ServerResponse.ok().body(
                friendshipService.findFriendshipNotificationsByFriendId(
                        request.pathVariable("friend-id"), request.headers().firstHeader(AUTHORIZATION),
                        Boolean.parseBoolean(request.queryParam("accepted").orElse(null))
                ),
                FriendshipNotification.class
        );
    }

    public Mono<ServerResponse> handleSendFriendRequest(ServerRequest request) {
        return ServerResponse.ok().body(
                friendshipService.sendFriendRequest(
                        request.bodyToMono(FriendshipRequest.class),
                        request.headers().firstHeader(AUTHORIZATION)
                ),
                String.class
        );
    }

    public Mono<ServerResponse> handleAcceptFriendship(ServerRequest request) {
        return ServerResponse.ok().body(
                friendshipService.acceptFriendship(
                        request.bodyToMono(FriendshipRequest.class),
                        request.headers().firstHeader(AUTHORIZATION)
                ),
                String.class
        );
    }

    public Mono<ServerResponse> handleDeclineFriendship(ServerRequest request) {
        return ServerResponse.ok().body(
                friendshipService.declineFriendship(
                        request.bodyToMono(FriendshipRequest.class),
                        request.headers().firstHeader(AUTHORIZATION)
                ),
                String.class
        );
    }

    public Mono<ServerResponse> handleDeleteFriendship(ServerRequest request) {
        return ServerResponse.ok().body(
                friendshipService.deleteFromFriendship(
                        request.bodyToMono(FriendshipRequest.class),
                        request.headers().firstHeader(AUTHORIZATION)
                ),
                String.class
        );
    }
}
