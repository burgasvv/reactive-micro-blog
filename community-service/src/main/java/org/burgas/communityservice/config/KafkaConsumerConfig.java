package org.burgas.communityservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.burgas.communityservice.dto.IdentityCommunityNotification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public Map<String, Object> consumerConfigs() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                ConsumerConfig.GROUP_ID_CONFIG, "identity-community-group-id",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                JsonDeserializer.TYPE_MAPPINGS,
                "org.burgas.communityservice.dto.IdentityCommunityNotification:" +
                "org.burgas.communityservice.dto.IdentityCommunityNotification"
        );
    }

    @Bean
    public ConsumerFactory<String, IdentityCommunityNotification> identityCommunityConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, IdentityCommunityNotification> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, IdentityCommunityNotification> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(identityCommunityConsumerFactory());
        return factory;
    }
}
