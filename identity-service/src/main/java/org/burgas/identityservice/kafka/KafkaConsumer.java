package org.burgas.identityservice.kafka;

import org.burgas.identityservice.dto.FriendshipNotification;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(groupId = "friend-notification-group-id", topics = "friend-notification-topic")
    public FriendshipNotification getIdentityFriend(FriendshipNotification friendshipNotification) {
        System.out.println(friendshipNotification);
        return friendshipNotification;
    }
}
