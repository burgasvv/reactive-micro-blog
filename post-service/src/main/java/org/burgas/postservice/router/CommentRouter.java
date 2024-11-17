package org.burgas.postservice.router;

import lombok.RequiredArgsConstructor;
import org.burgas.postservice.handler.CommentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class CommentRouter {

    private final CommentHandler commentHandler;

    @Bean
    public RouterFunction<ServerResponse> comments() {
        return RouterFunctions.route()
                .GET("/comments/in-post", commentHandler::handleFindByPostId)
                .POST("/comments/create", commentHandler::handleCreateOrUpdate)
                .PUT("/comments/update", commentHandler::handleCreateOrUpdate)
                .DELETE("/comments/delete", commentHandler::handleDelete)
                .build();
    }
}
