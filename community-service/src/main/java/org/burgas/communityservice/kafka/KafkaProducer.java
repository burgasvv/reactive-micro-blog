package org.burgas.communityservice.kafka;

import lombok.RequiredArgsConstructor;
import org.burgas.communityservice.dto.IdentityCommunityNotification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, IdentityCommunityNotification> kafkaTemplate;

    public Mono<Void> sendIdentityCommunityNotification(
            Mono<IdentityCommunityNotification> identityCommunityNotificationMono
    ) {
        return identityCommunityNotificationMono.flatMap(
                identityCommunityNotification ->
                        Mono.fromCallable(
                                () -> kafkaTemplate.send(
                                        "identity-community-topic",
                                        String.valueOf(identityCommunityNotification.getIdentityResponse().getId()),
                                        identityCommunityNotification
                                )
                        )
                                .then(Mono.empty())
        );
    }
}
