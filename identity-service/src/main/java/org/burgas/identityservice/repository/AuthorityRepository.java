package org.burgas.identityservice.repository;

import org.burgas.identityservice.entity.Authority;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends ReactiveCrudRepository<Authority, Long> {
}
