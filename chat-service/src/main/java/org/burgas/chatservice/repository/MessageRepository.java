package org.burgas.chatservice.repository;

import org.burgas.chatservice.entity.Message;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MessageRepository extends ReactiveCrudRepository<Message, Long> {

    Flux<Message> findMessagesByChatId(Long chatId);
}
