package org.burgas.postservice.handler;

import lombok.RequiredArgsConstructor;
import org.burgas.postservice.dto.CommentRequest;
import org.burgas.postservice.dto.CommentResponse;
import org.burgas.postservice.service.CommentService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class CommentHandler {

    private final CommentService commentService;

    public Mono<ServerResponse> handleFindByPostId(ServerRequest request) {
        return ServerResponse.ok().body(
                commentService.findByPostId(
                        request.queryParam("postId").orElse(null),
                        request.headers().firstHeader(AUTHORIZATION)
                ),
                CommentResponse.class
        );
    }

    public Mono<ServerResponse> handleCreateOrUpdate(ServerRequest request) {
        return ServerResponse.ok().body(
                commentService.createOrUpdate(
                        request.bodyToMono(CommentRequest.class),
                        request.headers().firstHeader(AUTHORIZATION)
                ),
                CommentResponse.class
        );
    }

    public Mono<ServerResponse> handleDelete(ServerRequest request) {
        return ServerResponse.ok().body(
                commentService.delete(
                        request.queryParam("commentId").orElse(null),
                        request.queryParam("identityId").orElse(null),
                        request.headers().firstHeader(AUTHORIZATION)
                ),
                String.class
        );
    }
}
