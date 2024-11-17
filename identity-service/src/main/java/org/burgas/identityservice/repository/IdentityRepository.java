package org.burgas.identityservice.repository;

import org.burgas.identityservice.entity.Identity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IdentityRepository extends ReactiveCrudRepository<Identity, Long> {

    Mono<Identity> findIdentityByUsername(String username);
}
