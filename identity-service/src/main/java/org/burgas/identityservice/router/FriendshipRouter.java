package org.burgas.identityservice.router;

import lombok.RequiredArgsConstructor;
import org.burgas.identityservice.handler.FriendshipHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@RequiredArgsConstructor
public class FriendshipRouter {

    private final FriendshipHandler friendshipHandler;

    @Bean
    public RouterFunction<ServerResponse> friendship() {
        return RouterFunctions.route()
                .GET("/friendship/by-friend/{friend-id}", friendshipHandler::handleFindFriendshipNotificationsByFriendId)
                .POST("/friendship/send-friend-request", friendshipHandler::handleSendFriendRequest)
                .POST("/friendship/accept-friendship", friendshipHandler::handleAcceptFriendship)
                .POST("/friendship/decline-friendship", friendshipHandler::handleDeclineFriendship)
                .DELETE("/friendship/delete-from friendship", friendshipHandler::handleDeleteFriendship)
                .build();
    }
}
