package org.burgas.chatservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic chatTopic() {
        return TopicBuilder.name("chat-topic")
                .partitions(10)
                .replicas(1)
                .build();
    }
}
