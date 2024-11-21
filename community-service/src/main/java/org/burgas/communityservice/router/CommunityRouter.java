package org.burgas.communityservice.router;

import lombok.RequiredArgsConstructor;
import org.burgas.communityservice.handler.CommunityHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@RequiredArgsConstructor
public class CommunityRouter {

    private final CommunityHandler communityHandler;

    @Bean
    public RouterFunction<ServerResponse> communities() {
        return RouterFunctions.route()
                .GET("/communities/{community-id}", communityHandler::handleFindById)
                .GET("/communities/by-identity", communityHandler::handleFindCommunitiesByIdentityId)
                .POST("/communities/create", communityHandler::handleCreateCommunity)
                .POST("/communities/join-the-community", communityHandler::handleJoinTheCommunity)
                .POST("/communities/leave-the-community", communityHandler::handleLeaveTheCommunity)
                .build();
    }
}
