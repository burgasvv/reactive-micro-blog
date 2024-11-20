package org.burgas.identityservice.kafka;

import lombok.RequiredArgsConstructor;
import org.burgas.identityservice.dto.FriendshipNotification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, FriendshipNotification> kafkaTemplate;

    public Mono<Void> sendIdentityFriend(Mono<FriendshipNotification> friendshipNotificationMono, Boolean answer) {
        return friendshipNotificationMono.flatMap(
                friendshipNotification -> {
                    if (answer) {
                        kafkaTemplate.send(
                                "friend-notification-topic",
                                String.valueOf(friendshipNotification.getFriendId()),
                                friendshipNotification
                        );
                    } else {
                        kafkaTemplate.send(
                                "friend-notification-topic",
                                String.valueOf(friendshipNotification.getIdentityId()),
                                friendshipNotification
                        );
                    }
                    return Mono.empty();
                }
        );
    }
}
