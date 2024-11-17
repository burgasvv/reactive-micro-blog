package org.burgas.postservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.postservice.dto.CommentRequest;
import org.burgas.postservice.dto.CommentResponse;
import org.burgas.postservice.dto.IdentityPrincipal;
import org.burgas.postservice.handler.WebClientHandler;
import org.burgas.postservice.mapper.CommentMapper;
import org.burgas.postservice.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = SUPPORTS)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final WebClientHandler webClientHandler;

    public Flux<CommentResponse> findByPostId(String postId, String authValue) {
        return commentRepository.findCommentsByPostId(Long.valueOf(postId))
                .flatMap(
                        comment -> commentMapper.toCommentResponse(Mono.just(comment), authValue)
                )
                .log("COMMENT_SERVICE::findByPostId");
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED, rollbackFor = Exception.class
    )
    public Mono<CommentResponse> createOrUpdate(Mono<CommentRequest> commentRequestMono, String authValue) {
        return Mono.zip(webClientHandler.getPrincipal(authValue), commentRequestMono)
                .flatMap(
                        objects -> {
                            IdentityPrincipal identityPrincipal = objects.getT1();
                            CommentRequest commentRequest = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    Objects.equals(identityPrincipal.getId(), commentRequest.getIdentityId())
                            ) {
                                return commentMapper.toComment(Mono.just(commentRequest))
                                        .flatMap(commentRepository::save)
                                        .flatMap(
                                                comment -> commentMapper.toCommentResponse(Mono.just(comment), authValue)
                                        )
                                        .log("COMMENT_SERVICE::createOrUpdate");
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет доступа")
                                );
                        }
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED, rollbackFor = Exception.class
    )
    public Mono<String> delete(String commentId, String identityId, String authValue) {
        return webClientHandler.getPrincipal(authValue)
                .flatMap(
                        identityPrincipal -> {
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    identityPrincipal.getId() == Long.parseLong(identityId)
                            ) {
                                return commentRepository.deleteById(Long.valueOf(commentId))
                                        .then(Mono.just(
                                                "Комментарий с идентификатором " + commentId + " успешно удален")
                                        )
                                        .log("COMMENT_SERVICE::delete");
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет доступа")
                                );
                        }
                );
    }
}
