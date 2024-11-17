package org.burgas.postservice.router;

import lombok.RequiredArgsConstructor;
import org.burgas.postservice.handler.PostHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@RequiredArgsConstructor
public class PostRouter {

    private final PostHandler postHandler;

    @Bean
    public RouterFunction<ServerResponse> posts() {
        return RouterFunctions.route()
                .GET("/posts/by-identity", postHandler::handleFindByIdentityId)
                .POST("/posts/create", postHandler::handleCreateOrUpdate)
                .PUT("/posts/update", postHandler::handleCreateOrUpdate)
                .DELETE("/posts/delete", postHandler::handleDelete)
                .build();
    }
}
