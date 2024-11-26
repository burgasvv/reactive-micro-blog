package org.burgas.communityservice.repository;

import org.burgas.communityservice.entity.CommunityInvitation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CommunityInvitationRepository extends ReactiveCrudRepository<CommunityInvitation, Long> {

    @Query(
            value = """
                    select distinct ci.* from community_invitation ci
                                where ci.receiver_id = :receiver_id and ci.is_accepted = false
                    """
    )
    Flux<CommunityInvitation> findCommunityInvitationsByReceiverIdAndIsAcceptedFalse(Long receiverId);

    @Query(
            value = """
                    select distinct ci.* from community_invitation ci
                                where ci.receiver_id = :receiver_id and ci.is_accepted = true
                    """
    )
    Flux<CommunityInvitation> findCommunityInvitationsByReceiverIdAndIsAcceptedTrue(Long receiverId);
}
