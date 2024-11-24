package org.burgas.communityservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic identityCommunityTopic() {
        return TopicBuilder.name("identity-community-topic")
                .partitions(10)
                .replicas(1)
                .build();
    }
}
