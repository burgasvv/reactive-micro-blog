package org.burgas.identityservice.repository;

import org.burgas.identityservice.entity.Identity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IdentityRepository extends ReactiveCrudRepository<Identity, Long> {

    Mono<Identity> findIdentityByUsername(String username);

    @Query(
            value = """
                    select distinct i.* from identity i join friendship f on i.id = f.friend_id
                    where f.identity_id = :identityId and f.accepted = true
                    """
    )
    Flux<Identity> findFriendsByIdentityId(Long identityId);

    @Query(
            value = """
                    select distinct i.* from identity i join identity_community ic on i.id = ic.identity_id
                    where ic.community_id = :communityId
                    """
    )
    Flux<Identity> findIdentitiesByCommunityId(Long communityId);
}
