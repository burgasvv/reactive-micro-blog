package org.burgas.communityservice.repository;

import org.burgas.communityservice.entity.Community;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CommunityRepository extends ReactiveCrudRepository<Community, Long> {

    @Modifying
    @Query(
            value = """
                    insert into identity_community(identity_id, community_id, owner)
                    values (:identityId, :communityId, true)
                    """
    )
    Mono<Void> insertCommunityCreator(Long identityId, Long communityId);

    @Modifying
    @Query(
            value = """
                    insert into identity_community(identity_id, community_id, owner)
                    values (:identityId, :communityId, false)
                    """
    )
    Mono<Void> insertSubscriber(Long identityId, Long communityId);

    @Modifying
    @Query(
            value = """
                    delete from identity_community where identity_id = :identityId and community_id = :communityId
                    """
    )
    Mono<Void> deleteSubscriber(Long identityId, Long communityId);

    @Query(
            value = """
                    select distinct c.* from community c join identity_community ic on c.id = ic.community_id
                    where ic.identity_id = :identityId
                    """
    )
    Flux<Community> findCommunitiesByIdentityId(Long identityId);

    @Modifying
    @Query(
            value = """
                    insert into wall(community_id, is_opened) values (:communityId, :isOpened)
                    """
    )
    Mono<Void> createCommunityWallByCommunityId(Long communityId, Boolean isOpened);
}
