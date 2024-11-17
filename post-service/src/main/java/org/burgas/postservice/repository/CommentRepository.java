package org.burgas.postservice.repository;

import org.burgas.postservice.entity.Comment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CommentRepository extends ReactiveCrudRepository<Comment, Long> {

    Flux<Comment> findCommentsByPostId(Long postId);
}
