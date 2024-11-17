package org.burgas.postservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.postservice.dto.IdentityPrincipal;
import org.burgas.postservice.dto.PostRequest;
import org.burgas.postservice.dto.PostResponse;
import org.burgas.postservice.handler.WebClientHandler;
import org.burgas.postservice.mapper.PostMapper;
import org.burgas.postservice.repository.PostRepository;
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
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final WebClientHandler webClientHandler;

    public Flux<PostResponse> findByIdentityId(String identityId, String authValue) {
        return postRepository.findPostsByIdentityId(Long.valueOf(identityId))
                .flatMap(post -> postMapper.toPostResponse(Mono.just(post), authValue))
                .log("POST_SERVICE::findByIdentityId");
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED, rollbackFor = Exception.class
    )
    public Mono<PostResponse> createOrUpdate(Mono<PostRequest> postRequestMono, String authValue) {
        return Mono.zip(webClientHandler.getPrincipal(authValue), postRequestMono)
                .flatMap(
                        objects -> {
                            IdentityPrincipal identityPrincipal = objects.getT1();
                            PostRequest postRequest = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    Objects.equals(identityPrincipal.getId(), postRequest.getIdentityId())
                            ) {
                                return postMapper.toPost(Mono.just(postRequest))
                                        .flatMap(postRepository::save)
                                        .flatMap(post -> postMapper.toPostResponse(Mono.just(post), authValue));
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован или не имеет прав доступа")
                                );
                        }
                )
                .log("POST_SERVICE::createOrUpdate");
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED, rollbackFor = Exception.class
    )
    public Mono<String> delete(String postId, String identityId, String authValue) {
        return webClientHandler.getPrincipal(authValue)
                .filter(
                        identityPrincipal -> identityPrincipal.getAuthenticated() &&
                                             identityPrincipal.getId() == Long.parseLong(identityId)
                )
                .flatMap(
                        identityPrincipal -> postRepository.deleteById(identityPrincipal.getId())
                                .then(Mono.fromCallable(() -> "Пост с идентификатором " + postId + " успешно удален"))
                )
                .switchIfEmpty(
                        Mono.error(new RuntimeException("Пользователь не авторизован или не имеет прав доступа"))
                )
                .log("POST_SERVICE::delete");

    }
}
