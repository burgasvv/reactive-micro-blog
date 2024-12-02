package org.burgas.postservice.repository;

import org.burgas.postservice.entity.Post;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PostRepository extends ReactiveCrudRepository<Post, Long> {

    Flux<Post> findPostsByIdentityId(Long id);

    Flux<Post> findPostsByWallId(Long wallId);
}
