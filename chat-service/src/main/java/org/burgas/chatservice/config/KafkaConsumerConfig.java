package org.burgas.chatservice.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.burgas.chatservice.dto.MessageResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TYPE_MAPPINGS;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public Map<String, Object> consumerConfigs() {
        return Map.of(
                BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                GROUP_ID_CONFIG, "chat-topic-group-id",
                AUTO_OFFSET_RESET_CONFIG, "earliest",
                KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                TYPE_MAPPINGS, "org.burgas.chatservice.dto.MessageResponse:org.burgas.chatservice.dto.MessageResponse"
        );
    }

    @Bean
    public ConsumerFactory<String, MessageResponse> messageResponseConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageResponse> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageResponse> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(messageResponseConsumerFactory());
        return factory;
    }
}
