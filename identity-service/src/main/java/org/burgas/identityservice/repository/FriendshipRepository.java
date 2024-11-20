package org.burgas.identityservice.repository;

import org.burgas.identityservice.entity.Friendship;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FriendshipRepository extends ReactiveCrudRepository<Friendship, Long> {

    Flux<Friendship> findFriendshipsByFriendIdAndAcceptedIsFalse(Long friendId);

    Flux<Friendship> findFriendshipsByFriendIdAndAcceptedIsTrue(Long friendId);

    @Modifying
    @Query(
            value = """
                    update friendship set accepted = true where identity_id = :identityId and friend_id = :friendId
                    """
    )
    Mono<Friendship> acceptFriendshipByIdentityIdAndFriendId(Long identityId, Long friendId);

    Mono<Friendship> findFriendshipByIdentityIdAndFriendIdAndAcceptedIsFalse(Long identityId, Long friendId);

    Mono<Void> deleteFriendshipByIdentityIdAndFriendIdAndAcceptedIsFalse(Long identityId, Long friendId);

    @Modifying
    @Query(
            value = """
                    delete from friendship where identity_id = :identityId and friend_id = :friendId and accepted = true
                    or identity_id = :friendId and friend_id = :identityId and accepted = true
                    """
    )
    Mono<Void> deleteFriendshipByIdentityIdAndFriendIdAndAcceptedIsTrue(Long identityId, Long friendId);
}
