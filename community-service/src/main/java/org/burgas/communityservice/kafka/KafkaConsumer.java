package org.burgas.communityservice.kafka;

import org.burgas.communityservice.dto.IdentityCommunityNotification;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(
            groupId = "identity-community-group-id", topics = "identity-community-topic"
    )
    public IdentityCommunityNotification getIdentityCommunityNotification(IdentityCommunityNotification notification) {
        System.out.println(notification);
        return notification;
    }
}
