package org.burgas.chatservice.repository;

import org.burgas.chatservice.entity.Chat;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatRepository extends ReactiveCrudRepository<Chat, Long> {

    @Query(
            value = """
                    select distinct * from chat where sender_id = :identityId or receiver_id = :identityId
                    """
    )
    Flux<Chat> findChatsByIdentityId(Long identityId);
}
