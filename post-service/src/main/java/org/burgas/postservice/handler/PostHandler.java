package org.burgas.postservice.handler;

import lombok.RequiredArgsConstructor;
import org.burgas.postservice.dto.PostRequest;
import org.burgas.postservice.dto.PostResponse;
import org.burgas.postservice.service.PostService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class PostHandler {

    private final PostService postService;

    public Mono<ServerResponse> handleFindByIdentityId(ServerRequest request) {
        return ServerResponse.ok().body(
                postService.findByIdentityId(
                        request.queryParam("identityId").orElse(null),
                        request.headers().firstHeader(AUTHORIZATION)
                ),
                PostResponse.class
        );
    }

    public Mono<ServerResponse> handleFindByWallId(ServerRequest request) {
        return ServerResponse.ok().body(
                postService.findByWallId(
                        request.queryParam("wallId").orElse(null),
                        request.headers().firstHeader(AUTHORIZATION)
                ),
                PostResponse.class
        );
    }

    public Mono<ServerResponse> handleCreateOrUpdate(ServerRequest request) {
        return ServerResponse.ok().body(
                postService.createOrUpdate(
                        request.bodyToMono(PostRequest.class),
                        request.headers().firstHeader(AUTHORIZATION)
                ),
                PostResponse.class
        );
    }

    public Mono<ServerResponse> handleDelete(ServerRequest request) {
        return ServerResponse.ok().body(
                postService.delete(
                        request.queryParam("postId").orElse(null),
                        request.queryParam("identityId").orElse(null),
                        request.headers().firstHeader(AUTHORIZATION)
                ),
                String.class
        );
    }
}
