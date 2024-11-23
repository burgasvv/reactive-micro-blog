package org.burgas.postservice.repository;

import org.burgas.postservice.entity.Wall;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WallRepository extends ReactiveCrudRepository<Wall, Long> {
}
