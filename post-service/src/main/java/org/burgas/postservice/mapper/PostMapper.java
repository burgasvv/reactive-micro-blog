package org.burgas.postservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.postservice.dto.PostRequest;
import org.burgas.postservice.dto.PostResponse;
import org.burgas.postservice.entity.Post;
import org.burgas.postservice.handler.WebClientHandler;
import org.burgas.postservice.repository.PostRepository;
import org.burgas.postservice.service.CommentService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final PostRepository postRepository;
    private final CommentService commentService;
    private final WebClientHandler webClientHandler;

    public Mono<Post> toPost(Mono<PostRequest> postRequestMono) {
        return postRequestMono.flatMap(
                postRequest -> {
                    Long postId = postRequest.getId() == null ? 0L : postRequest.getId();
                    return postRepository.findById(postId)
                            .mapNotNull(post -> post)
                            .flatMap(
                                    post -> Mono.fromCallable(
                                            () -> Post.builder()
                                                    .id(postRequest.getId())
                                                    .content(postRequest.getContent())
                                                    .identityId(postRequest.getIdentityId())
                                                    .publishedAt(post.getPublishedAt())
                                                    .isNew(false)
                                                    .build()
                                    )
                            )
                            .switchIfEmpty(
                                    Mono.fromCallable(
                                            () -> Post.builder()
                                                    .id(postRequest.getId())
                                                    .content(postRequest.getContent())
                                                    .identityId(postRequest.getIdentityId())
                                                    .publishedAt(LocalDateTime.now())
                                                    .isNew(true)
                                                    .build()
                                    )
                            );
                }
        );
    }

    public Mono<PostResponse> toPostResponse(Mono<Post> postMono, String authValue) {
        return postMono.flatMap(
                post -> webClientHandler.getIdentityById(post.getIdentityId(), authValue)
                        .flatMap(
                                identityResponse -> commentService.findByPostId(
                                        String.valueOf(post.getId()), authValue
                                ).collectList()
                                        .flatMap(
                                                commentResponses -> Mono.fromCallable(
                                                        () -> PostResponse.builder()
                                                                .id(post.getId())
                                                                .content(post.getContent())
                                                                .identityResponse(identityResponse)
                                                                .commentResponses(commentResponses)
                                                                .publishedAt(
                                                                        post.getPublishedAt().format(
                                                                                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                                                                        )
                                                                )
                                                                .build()
                                                )
                                        )
                        )
        );
    }
}
