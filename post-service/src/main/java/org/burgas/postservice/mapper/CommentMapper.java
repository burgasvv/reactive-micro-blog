package org.burgas.postservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.postservice.dto.CommentRequest;
import org.burgas.postservice.dto.CommentResponse;
import org.burgas.postservice.entity.Comment;
import org.burgas.postservice.handler.CryptHandler;
import org.burgas.postservice.handler.WebClientHandler;
import org.burgas.postservice.repository.CommentRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final CommentRepository commentRepository;
    private final WebClientHandler webClientHandler;
    private final CryptHandler cryptHandler;

    public Mono<Comment> toComment(Mono<CommentRequest> commentRequestMono) {
        return commentRequestMono.flatMap(
                commentRequest -> {
                    Long commentId = commentRequest.getId() == null ? 0L : commentRequest.getId();
                    return commentRepository.findById(commentId)
                            .mapNotNull(comment -> comment)
                            .flatMap(
                                    comment -> Mono.fromCallable(
                                            () -> Comment.builder()
                                                    .id(commentRequest.getId())
                                                    .identityId(commentRequest.getIdentityId())
                                                    .postId(commentRequest.getPostId())
                                                    .content(cryptHandler.encrypt(commentRequest.getContent()))
                                                    .publishedAt(comment.getPublishedAt())
                                                    .isNew(false)
                                                    .build()
                                    )
                            )
                            .switchIfEmpty(
                                    Mono.fromCallable(
                                            () -> Comment.builder()
                                                    .id(commentRequest.getId())
                                                    .identityId(commentRequest.getIdentityId())
                                                    .postId(commentRequest.getPostId())
                                                    .content(cryptHandler.encrypt(commentRequest.getContent()))
                                                    .publishedAt(LocalDateTime.now())
                                                    .isNew(true)
                                                    .build()
                                    )
                            );
                }
        );
    }

    public Mono<CommentResponse> toCommentResponse(Mono<Comment> commentMono, String authValue) {
        return commentMono.flatMap(
                comment -> webClientHandler.getIdentityById(comment.getIdentityId(), authValue)
                        .flatMap(
                                identityResponse -> Mono.fromCallable(
                                        () -> CommentResponse.builder()
                                                .id(comment.getId())
                                                .postId(comment.getPostId())
                                                .content(cryptHandler.decrypt(comment.getContent()))
                                                .identityResponse(identityResponse)
                                                .publishedAt(
                                                        comment.getPublishedAt().format(
                                                                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                                                        )
                                                )
                                                .build()
                                )
                        )
        );
    }
}
