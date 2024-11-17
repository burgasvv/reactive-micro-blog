package org.burgas.chatservice.repository;

import org.burgas.chatservice.entity.Chat;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends ReactiveCrudRepository<Chat, Long> {
}
