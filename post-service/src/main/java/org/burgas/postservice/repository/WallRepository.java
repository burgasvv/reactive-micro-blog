package org.burgas.postservice.repository;

import org.burgas.postservice.entity.Wall;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WallRepository extends ReactiveCrudRepository<Wall, Long> {

    @Query(
            value = """
                    select i.identity_id from wall i where i.id = :wallId
                    """
    )
    Mono<Long> findIdentityIdByWallId(Long wallId);

    @Query(
            value = """
                    select i.community_id from wall i where i.id = :wallId
                    """
    )
    Mono<Long> findCommunityIdByWallId(Long wallId);

    @Query(
            value = """
                    select ic.identity_id from identity_community ic where ic.community_id = :communityId and owner = true
                    """
    )
    Mono<Long> findIdentityIdByCommunityId(Long communityId);
}
